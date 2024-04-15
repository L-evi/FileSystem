package com.levi.converter;

import com.levi.mapper.file.FileEntity;
import com.levi.model.request.FileRequest;
import com.levi.model.view.FileView;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class FileConverter {

    @Mappings({})
    public abstract FileView entity2View(FileEntity fileEntity);

    @Mappings({})
    public abstract List<FileView> entity2View(List<FileEntity> fileEntities);

    @Mappings({})
    public abstract FileEntity request2Entity(FileRequest fileRequest);
}
