package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.dto.LinkPageDto;
import top.skmcj.liushu.entity.Link;
import top.skmcj.liushu.service.LinkService;
import top.skmcj.liushu.vo.LinkVo;

import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 添加友情链接
     * @param link
     * @return
     */
    @PostMapping
    public Result<String> addLink(@RequestBody Link link) {
        /**
         * 防篡改
         */
        link.setId(null);
        link.setFlag(0);
        boolean flag = linkService.save(link);
        if(flag) {
            return Result.success(StatusCodeEnum.ADD_OK);
        } else {
            return Result.error(StatusCodeEnum.ADD_ERR);
        }
    }

    /**
     * 修改友情链接
     * @param link
     * @return
     */
    @PutMapping
    public Result<String> updateLink(@RequestBody Link link) {
        if(link.getId() == null) {
            return Result.error("链接ID不能为空");
        }
        link.setFlag(null);
        boolean flag = linkService.updateById(link);
        if(!flag) return Result.error(StatusCodeEnum.UPDATE_ERR);
        return Result.success(StatusCodeEnum.UPDATE_OK);
    }

    /**
     * 根据ID获取友情链接 - 只能获取到审核通过的
     * @param id
     * @return
     */
    @GetMapping("/id")
    public Result<Link> getLinkById(Long id) {
        Link link = linkService.getById(id);
        if(link == null) return Result.error("找不到相应的友情链接");
        if(link.getFlag() != 1) {
            return Result.error("链接" + link.getProcess());
        }
        return Result.success(link, StatusCodeEnum.GET_OK);
    }

    /**
     * 根据ID获取友情链接 - 包含所有状态
     * @param id
     * @return
     */
    @GetMapping("/pid")
    public Result<Link> getPLinkById(Long id) {
        Link link = linkService.getById(id);
        if(link == null) return Result.error("找不到相应的友情链接");
        return Result.success(link, StatusCodeEnum.GET_OK);
    }

    /**
     * 根据类型获取友情链接
     * @param type
     * @return
     */
    @GetMapping("/type")
    public Result<LinkPageDto> getLinkByType(String type) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getType, type);
        queryWrapper.eq(Link::getFlag, 1);
        List<Link> list = linkService.list(queryWrapper);
        if(list == null || list.size() == 0) {
            return Result.error("找不到该类型的链接");
        }
        LinkPageDto<Link> linkList = new LinkPageDto<>();
        linkList.setRows(list);
        linkList.setTotal(list.size());
        linkList.setType(type);
        linkList.setInfo(this.getTypeInfo(type));
        return Result.success(linkList, StatusCodeEnum.GET_OK);
    }

    /**
     * 根据类型获取友情链接
     * @param type
     * @return
     */
    @GetMapping("/ptype")
    public Result<LinkPageDto> getPLinkByType(String type) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getType, type);
        List<Link> list = linkService.list(queryWrapper);
        if(list == null || list.size() == 0) {
            return Result.error("找不到该类型的链接");
        }
        LinkPageDto<Link> linkList = new LinkPageDto<>();
        linkList.setRows(list);
        linkList.setTotal(list.size());
        linkList.setType(type);
        linkList.setInfo(this.getTypeInfo(type));
        return Result.success(linkList, StatusCodeEnum.GET_OK);
    }

    /**
     * 获取全部友情链接
     * @return
     */
    @GetMapping("/all")
    public Result<LinkPageDto> getLinkAll() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getFlag, 1);
        List<Link> list = linkService.list(queryWrapper);
        if(list == null || list.size() == 0) {
            return Result.error("暂无友情链接");
        }
        LinkPageDto<Link> linkList = new LinkPageDto<>();
        linkList.setRows(list);
        linkList.setTotal(list.size());
        linkList.setType("all");
        linkList.setInfo(this.getTypeInfo("all"));
        return Result.success(linkList, StatusCodeEnum.GET_OK);
    }

    /**
     * 获取全部友情链接
     * @return
     */
    @GetMapping("/pall")
    public Result<LinkPageDto> getPLinkAll() {
        List<Link> list = linkService.list();
        if(list == null || list.size() == 0) {
            return Result.error("暂无友情链接");
        }
        LinkPageDto<Link> linkList = new LinkPageDto<>();
        linkList.setRows(list);
        linkList.setTotal(list.size());
        linkList.setType("all");
        linkList.setInfo(this.getTypeInfo("all"));
        return Result.success(linkList, StatusCodeEnum.GET_OK);
    }

    /**
     * 根据id删除友情链接
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> deleteLinkById(@RequestParam Long id) {
        boolean flag = linkService.removeById(id);
        if(!flag) return Result.error(StatusCodeEnum.DELETE_ERR);
        return Result.success(StatusCodeEnum.DELETE_OK);
    }

    /**
     * 根据ID审核友情链接
     * @param linkVo
     * @return
     */
    @PutMapping("/process")
    public Result<String> processLinkById(@RequestBody LinkVo linkVo) {
        if(linkVo.getId() == null) {
            return Result.error("没有提供审核ID");
        }
        if(linkVo.getFlag() == null) {
            return Result.error("没有提供目标审核状态");
        }
        Link link = new Link();
        link.setId(linkVo.getId());
        link.setFlag(linkVo.getFlag());
        boolean flag = linkService.updateById(link);
        if(!flag) return Result.error(StatusCodeEnum.UPDATE_ERR);
        return Result.success(StatusCodeEnum.UPDATE_OK);
    }

    /**
     * 获取类型说明
     * @param type
     * @return
     */
    private String getTypeInfo(String type) {
        switch (type) {
            case "image":
                return "图片链接";
            case "text":
                return "文本链接";
            case "all":
                return "全部类型";
            default:
                return "未知类型";
        }
    }
}
