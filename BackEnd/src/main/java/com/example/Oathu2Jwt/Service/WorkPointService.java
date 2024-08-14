package com.example.Oathu2Jwt.Service;

import com.example.Oathu2Jwt.Model.Entity.WorkPoint;

public interface WorkPointService {
    public WorkPoint createWorkPoint(WorkPoint workPoint);
    public WorkPoint checkWorkPoint(String userId);
}
