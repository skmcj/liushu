package top.skmcj.liushu.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.skmcj.liushu.entity.Book;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<Book> {

    /**
     * 获取指定条数的记录
     *
     * @param start
     * @param size
     * @return
     */
    @Select("SELECT id,store_id,cover,name,author,book_cate_id,goods_cate_id,press,pub_date,size,pages,inventory," +
            "status,mba,tba,create_time,update_time,create_user,update_user,is_deleted FROM book WHERE is_deleted=0 " +
            "ORDER BY mba DESC, tba DESC, update_time DESC LIMIT #{start},#{size}")
    List<Book> getBookLimit(int start, int size);
}
