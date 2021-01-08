package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName CategoryEntity
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2020/12/22
 * @Version V1.0
 **/
@Data
@Table(name = "tb_category")
@ApiModel(value = "分类实体类")
public class CategoryEntity {
    @Id
    @ApiModelProperty(value = "类目id",example = "1")
    @NotNull(message = "id不能为空",groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value = "类目名称")
    @NotEmpty(message = "分类名称不能为null",groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    private String name;

    @ApiModelProperty(value = "父类目id,顶级类目填0",example = "1")
    @NotNull(message = "父级分类不能为null",groups = {MingruiOperation.add.class})
    private Integer parentId;

    @ApiModelProperty(value = "是否为父节点，0为否，1为是",example = "1")
    @NotNull(message = "是否为父节点不能为null",groups = {MingruiOperation.add.class})
    private Integer isParent;

    @ApiModelProperty(value = "排序指数，越小越靠前",example = "1")
    @NotNull(message = "排序不能为null",groups = {MingruiOperation.add.class})
    private Integer sort;
}
