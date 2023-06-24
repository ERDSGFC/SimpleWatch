package com.qin.common;


import java.util.ArrayList;
import java.util.List;

public interface BaseMapStruct<D,E> {

    /**
     * DTO转Entity
     * @param dto /
     * @return /
     */
    E toEntity(D dto);

    /**
     * Entity转DTO
     * @param entity /
     * @return /
     */
    D toDto(E entity);

    /**
     * DTO集合转Entity集合
     * @param dtoList /
     * @return /
     */
    ArrayList<E> toEntity(List<D> dtoList);

    /**
     * Entity集合转DTO集合
     * @param entityList /
     * @return /
     */
    ArrayList <D> toDto(List<E> entityList);

//    /**
//     * 关联转换
//     * @param myData /
//     * @return /
//     */
//    D toAppendDto(MyData<E> myData);
//
//    /**
//     * 关联转换
//     * @param myDataList /
//     * @return /
//     */
//    ArrayList <D> toAppendDto(List<MyData<E>> myDataList);
//
//    /**
//     * 分页的转换
//     * @param myData /
//     * @param pojoList /
//     * @return /
//     */
//    Page<D> toPageDto(ArrayList<MyData<E>> myData, Page<E> pojoList);
}
