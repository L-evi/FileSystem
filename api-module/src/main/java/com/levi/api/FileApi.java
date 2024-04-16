package com.levi.api;

import com.dtflys.forest.annotation.Post;
import com.levi.model.PageRequest;
import com.levi.model.PageView;
import com.levi.model.request.FileRequest;
import com.levi.model.view.FileView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@RequestMapping("/file")
public interface FileApi {

    @Post("/file/query/list")
    @PostMapping("/query/list")
    PageView<FileView> pageQuery(@RequestBody PageRequest<FileRequest> fileRequestPageRequest);

    @Post("/file/query/detail")
    @PostMapping("/query/detail")
    FileView detailByFileId(Long fileId);

    @Post("/file/batch/delete")
    @PostMapping("/batch/delete")
    Integer deleteByFileId(Set<Long> fileIds);

    @Post("/file/batch/upload")
    @PostMapping("/batch/upload")
    FileView batchUpload(@RequestBody FileRequest fileRequest);
}
