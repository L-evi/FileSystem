package com.levi.api;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Post;
import com.levi.model.PageRequest;
import com.levi.model.PageView;
import com.levi.model.request.FileRequest;
import com.levi.model.view.FileView;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/file")
public interface FileApi {

    @Post("/file/query/list")
    @PostMapping("/query/list")
    PageView<FileView> pageQuery(@RequestBody PageRequest<FileRequest> fileRequestPageRequest);

    @Get("/file/query/detail")
    @GetMapping("/query/detail")
    FileView detailByFileId(@RequestParam("fileId") Long fileId);

    @Post("/file/batch/delete")
    @PostMapping("/batch/delete")
    Integer deleteByFileId(Set<Long> fileIds);

    @Post("/file/batch/upload")
    @PostMapping("/batch/upload")
    List<FileView> batchUpload(FileRequest fileRequest);

    @Post("/file/upload")
    @PostMapping("/file/upload")
    FileView fileUpload(FileRequest fileRequest);
}
