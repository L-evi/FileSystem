package com.levi.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.levi.model.constant.SystemConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PageView<T> {
    @Schema(description = "页数据量")
    private Long pageSize = SystemConstant.ZERO_VALUE;

    @Schema(description = "页码")
    private Long pageNumber = SystemConstant.TEN_VALUE;

    @Schema(description = "数据总量")
    private Long dataCount;

    private List<T> data;


    public <M> PageView(Page<M> tPage, List<T> data) {
        this.pageSize = tPage.getSize();
        this.pageNumber = tPage.getCurrent();
        this.dataCount = tPage.getTotal();
        this.data = data;
    }
}
