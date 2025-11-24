package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Transfer;
import com.example.mapper.TransferMapper;
import com.example.service.TransferService;
import org.springframework.stereotype.Service;

/**
 * 调宿申请Service实现
 */
@Service
public class TransferServiceImpl extends ServiceImpl<TransferMapper, Transfer> implements TransferService {
}
