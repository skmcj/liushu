package top.skmcj.liushu.util;

import java.util.List;

/**
 * 数据操作增强工具
 */
public class DataHandleUtil {

    /**
     * 向指定列表添加元素
     * 列表只会保存最新的 size 个元素
     * @param list 目标列表
     * @param item 待添加元素
     * @param size 列表大小
     */
    public static void addToLimitedList(List list, Object item, int size) {
        if(list.size() >= size) {
            list.remove(0);
        }
        list.add(item);
    }
}
