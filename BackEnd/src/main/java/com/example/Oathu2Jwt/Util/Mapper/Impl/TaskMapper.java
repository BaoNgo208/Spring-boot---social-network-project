package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.TaskDTO;
import com.example.Oathu2Jwt.Model.Entity.Task;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper implements Mapper<Task, TaskDTO> {
    private final ModelMapper modelMapper;
    @Override
    public TaskDTO mapTo(Task task) {
        return modelMapper.map(task,TaskDTO.class);
    }

    @Override
    public Task mapFrom(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO,Task.class);
    }
}
