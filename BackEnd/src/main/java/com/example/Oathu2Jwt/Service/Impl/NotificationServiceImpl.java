package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Notification.ArchivedNotification;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Notification.Notification;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ArchivedNotificationRepo;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.NotificationRepo;
import com.example.Oathu2Jwt.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;
    private final RedisTemplate<String,Object> redisTemplate;
    private final ArchivedNotificationRepo archivedNotificationRepo;

    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void archiveOldNotification() {
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30));
        List<Notification> oldNotification =  notificationRepo.findByCreatedAtBefore(thirtyDaysAgo);
        for(Notification notification : oldNotification) {
            ArchivedNotification archivedNotification = ArchivedNotification.builder()
                    .id(notification.getId())
                    .type(notification.getType())
                    .senderId(notification.getSenderId())
                    .senderName(notification.getSenderName())
                    .receiverId(notification.getReceiverId())
                    .createdAt(notification.getCreatedAt())
                    .archivedAt(new Date())
                    .build();
            archivedNotificationRepo.save(archivedNotification);
            notificationRepo.delete(notification);
        }
    }
    private Notification convertToNotification(ArchivedNotification archivedNotification) {
        Notification notification = new Notification();
        notification.setId(archivedNotification.getId());
        notification.setSenderId(notification.getSenderId());
        notification.setReceiverId(notification.getReceiverId());
        notification.setType(notification.getType());
        notification.setSenderName(archivedNotification.getSenderName());
        notification.setCreatedAt(notification.getCreatedAt());
        return notification;
    }

    @Override
    public Page<Notification> getNotificationOfUser(String userId,int page,int size) {
        Pageable pageable = PageRequest.of(page
                ,size
                , Sort.by(Sort.Direction.DESC,"createdAt"));

        String redisKey = "Notifications of:"+userId;
        List<Object> notificationFromRedis= new ArrayList<>();
        if(Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            notificationFromRedis.addAll(redisTemplate.opsForHash().values(redisKey));
        }
        Set<Notification> notificationSet = notificationFromRedis.stream()
                .filter(obj -> obj instanceof  Notification)
                .map(obj -> (Notification) obj)
                .collect(Collectors.toCollection(
                        ()-> new TreeSet<>(
                                Comparator.comparing(Notification::getId)
                        )
                ));
        List<Notification> notifications = new ArrayList<>(notificationSet);

        //if not found in redis,find in notification in mongodb
        if(notifications.isEmpty()) {
            Page<Notification> pageOfNotification = notificationRepo
                    .findByReceiverId(Long.parseLong(userId),pageable);
            notifications = pageOfNotification.getContent();
            for(Notification notification : notifications) {
                 String key = "Notifications of:" + notification.getReceiverId();
                 redisTemplate.opsForHash().put(key,notification.getId(),notification);
                 redisTemplate.expire(key, Duration.ofDays(7));
            }
        } else {
            Page<Notification> pageOfNotification = notificationRepo
                    .findByReceiverId(Long.parseLong(userId),pageable);
            notificationSet.addAll(pageOfNotification.getContent());
            notifications = new ArrayList<>(notificationSet);
        }

        //if not found in notification , find in archiveNotification mongodb
        if(notifications.isEmpty()) {
            Page<ArchivedNotification> pageOfNotification = archivedNotificationRepo.findByReceiverId(Long.parseLong(userId),pageable);
            notifications = pageOfNotification.getContent()
                    .stream().map(this::convertToNotification)
                    .toList();
        }

        List<Notification> sortedNotification = new ArrayList<>(notifications);
        sortedNotification.sort(Comparator.comparing(Notification::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedNotification.size());

        if (start > sortedNotification.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, sortedNotification.size());
        }

        List<Notification> pagedMessages = sortedNotification.subList(start, end);
        return new PageImpl<>(pagedMessages, pageable, sortedNotification.size());
    }

    @Override
    public void saveNotification(Notification notification) {
         notificationRepo.save(notification);
         String redisKey = "Notifications of:" +notification.getReceiverId();
         redisTemplate.opsForHash().put(redisKey,notification.getId(),notification);
         redisTemplate.expire(redisKey, Duration.ofDays(7));
    }


}
