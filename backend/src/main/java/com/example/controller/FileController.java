package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.config.FileUploadConfig;
import com.example.entity.File;
import com.example.exception.ServiceException;
import com.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 文件管理控制器
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileUploadConfig fileUploadConfig;

    /**
     * 分页查询文件列表
     */
    @GetMapping("/page")
    public Result<IPage<File>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer enable) {
        
        Page<File> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(fileName)) {
            wrapper.like(File::getFileName, fileName);
        }
        if (StringUtils.hasText(type)) {
            wrapper.eq(File::getType, type);
        }
        if (enable != null) {
            wrapper.eq(File::getEnable, enable);
        }
        
        wrapper.orderByDesc(File::getCreateTime);
        IPage<File> result = fileService.page(page, wrapper);
        
        return Result.success(result);
    }

    /**
     * 查询所有文件
     */
    @GetMapping("/list")
    public Result<List<File>> list() {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(File::getCreateTime);
        List<File> list = fileService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 根据ID查询文件
     */
    @GetMapping("/{id}")
    public Result<File> getById(@PathVariable Long id) {
        File file = fileService.getById(id);
        if (file == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "文件不存在");
        }
        return Result.success(file);
    }

    /**
     * 根据MD5查询文件（用于秒传）
     */
    @GetMapping("/md5/{md5}")
    public Result<File> getByMd5(@PathVariable String md5) {
        File file = fileService.getByMd5(md5);
        return Result.success(file);
    }

    /**
     * 文件上传
     */
    @Log("上传文件")
    @PostMapping("/upload")
    public Result<File> upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "上传文件不能为空");
        }

        // 获取原始文件名
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "文件名不能为空");
        }

        // 获取文件类型
        String fileType = FileUtil.extName(originalFilename);
        
        // 计算文件大小（KB）
        long size = multipartFile.getSize() / 1024;

        // 计算文件MD5，确保输入流及时关闭，避免占用临时文件
        String md5;
        try (java.io.InputStream inputStream = multipartFile.getInputStream()) {
            md5 = SecureUtil.md5(inputStream);
        }

        // 检查文件是否已存在（秒传功能）
        File existFile = fileService.getByMd5(md5);
        if (existFile != null) {
            return Result.success(existFile);
        }

        // 生成唯一文件名
        String uniqueFileName = IdUtil.simpleUUID() + "." + fileType;

        // 确保上传目录存在
        java.io.File uploadDir = new java.io.File(fileUploadConfig.getUploadPath());
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 保存文件到磁盘
        String filePath = fileUploadConfig.getUploadPath() + uniqueFileName;
        java.io.File dest = new java.io.File(filePath);
        multipartFile.transferTo(dest);

        // 生成访问URL
        String url = fileUploadConfig.getAccessUrl() + uniqueFileName;

        // 保存文件记录到数据库
        File file = new File();
        file.setFileName(originalFilename);
        file.setType(fileType);
        file.setSize(size);
        file.setUrl(url);
        file.setMd5(md5);
        file.setEnable(1);

        fileService.save(file);

        return Result.success(file);
    }


    /**
     * 新增文件记录（手动）
     */
    @Log("新增文件记录")
    @PostMapping
    public Result<Void> add(@RequestBody File file) {
        boolean success = fileService.save(file);
        return success ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改文件信息
     */
    @Log("修改文件信息")
    @PutMapping
    public Result<Void> update(@RequestBody File file) {
        if (file.getId() == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "文件ID不能为空");
        }
        
        File existFile = fileService.getById(file.getId());
        if (existFile == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "文件不存在");
        }
        
        boolean success = fileService.updateById(file);
        return success ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除文件（逻辑删除）
     */
    @Log("删除文件")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        File file = fileService.getById(id);
        if (file == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "文件不存在");
        }
        
        boolean success = fileService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除文件
     */
    @Log("批量删除文件")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "请选择要删除的文件");
        }
        
        boolean success = fileService.removeByIds(ids);
        return success ? Result.success() : Result.error("批量删除失败");
    }

    /**
     * 修改文件启用状态
     */
    @Log("修改文件状态")
    @PutMapping("/enable/{id}")
    public Result<Void> updateEnable(@PathVariable Long id, @RequestParam Integer enable) {
        File file = fileService.getById(id);
        if (file == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "文件不存在");
        }
        
        file.setEnable(enable);
        boolean success = fileService.updateById(file);
        return success ? Result.success() : Result.error("状态修改失败");
    }

    /**
     * 文件下载
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        File file = fileService.getById(id);
        if (file == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "文件不存在");
        }

        try {
            // 从URL中提取文件名
            String url = file.getUrl();
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            
            // 构建文件路径
            Path filePath = Paths.get(fileUploadConfig.getUploadPath()).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ServiceException(ResultCode.DATA_NOT_EXIST, "文件不存在或无法读取");
            }

            // 对文件名进行URL编码，支持中文文件名
            String encodedFileName = URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                    .body(resource);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "文件下载失败: " + e.getMessage());
        }
    }
}
