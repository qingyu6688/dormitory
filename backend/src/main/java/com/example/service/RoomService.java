package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Room;
import com.example.vo.RoomResidentVO;

import java.util.List;

/**
 * 宿舍Service
 */
public interface RoomService extends IService<Room> {
    
    /**
     * 查询宿舍住宿人员
     */
    List<RoomResidentVO> getRoomResidents(Long roomId);
    
    /**
     * 获取当前用户的宿舍信息
     */
    com.example.vo.MyRoomVO getMyRoom();
    
    /**
     * 获取室友列表
     */
    List<com.example.vo.RoommateVO> getRoommates(Long roomId);
    
    /**
     * 获取可用房间列表
     */
    List<Object> getAvailableRooms(Long buildingId);
    
    /**
     * 获取已占用床位
     */
    List<Integer> getOccupiedBeds(Long roomId);
    
    /**
     * 学生选择宿舍
     */
    void selectRoom(Long roomId, Integer bedNo);
}
