package com.levi.controller;

import com.levi.api.FileApi;
import com.levi.model.PageRequest;
import com.levi.model.PageView;
import com.levi.model.request.FileRequest;
import com.levi.model.view.FileView;
import com.levi.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class FileController implements FileApi {

    @Resource
    private FileService fileService;

    @Override
    public PageView<FileView> pageQuery(PageRequest<FileRequest> fileRequestPageRequest) {
        return fileService.pageQuery(fileRequestPageRequest);
    }

    @Override
    public FileView detailByFileFolderId(Long fileId) {
        return fileService.detailByFileFolderId(fileId);
    }

    @Override
    public Integer deleteByFileId(Set<Long> fileIds) {
        return fileService.deleteByFileId(fileIds);
    }

    @Override
    public List<FileView> batchUpload(FileRequest fileRequest) {
        return fileService.batchUpload(fileRequest);
    }

    @Override
    public FileView fileUpload(FileRequest fileRequest) {
        return fileService.fileUpload(fileRequest);
    }
}