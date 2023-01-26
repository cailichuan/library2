package com.cai.library.service.admin;

import com.cai.library.dao.admin.PageInput;
import com.cai.library.dao.admin.User;
import com.cai.library.dataCreate.pojo.CreateUuid;
import com.cai.library.exception.ServiceException;
import com.cai.library.mapper.admin.BookMapper;
import com.cai.library.mapper.admin.BorrowOfBookMapper;
import com.cai.library.mapper.admin.BorrowOfUserMapper;
import com.cai.library.mapper.admin.UserMapper;
import com.cai.library.pojo.DataTool;
import com.cai.library.pojo.GetPageInputOrMaxPage;
import com.cai.library.pojo.GetPageOffsetRows;
import com.cai.library.pojo.GetSnowId;
import com.cai.library.type.SnowIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    BorrowOfUserMapper borrowOfUserMapper;

    @Autowired
    BorrowOfBookMapper borrowOfBookMapper;
    @Autowired
    GetSnowId getSnowId;

    @Autowired
    GetPageInputOrMaxPage getPageInputOrMaxPage;

    @Autowired
    GetPageOffsetRows getPageOffsetRows;

    @Autowired
    CreateUuid createUuid;

    @Autowired
    DataTool dataTool;

    //根据页数获取该页数据和翻页组件的
    //map:{该页数据，翻页组件}
    public Map<String, Object> selectUserByPage(int page) {
        Map<String, Object> thisPageDataAndPageInputMap = new HashMap<>();
        //判断传入的页数是否合法或者搜索结果是否为空
        int dataNumber = userMapper.getDataNumber();
        int maxPage = getPageInputOrMaxPage.getMaxPage(dataNumber);
        if (page < 1 || page > maxPage || dataNumber == 0) {
            return null;
        }


        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(page);

        //该页数据和翻页组件的获取
        thisPageDataAndPageInputMap.put("thisPageData", userMapper.selectUserByPage(pageOffsetRowsMap));
        thisPageDataAndPageInputMap.put("PageInput", getPageInputOrMaxPage.getPageInput(page, dataNumber));

        return thisPageDataAndPageInputMap;
    }

    //获取修改数据
    public User getEditInfo(String id) {
        return userMapper.selectUserById(id);
    }

    //修改数据
    public int upDateUser(User user) {

        return userMapper.updateUser(user);
    }

    //添加数据
    public int addUser(User user) {
        user.setId(String.valueOf(getSnowId.setSonwIdType(SnowIdType.USER).nextId()));
        return userMapper.insertUser(user);
    }

    //删除数据
    @Transactional
    public void deleteUserById(String id) {
        try {
            Map<String,Object> idAndBorrowMap=new HashMap();
            idAndBorrowMap.put("userId",id);
            idAndBorrowMap.put("borrow",1);
            List<String> borrowOfUserIds = borrowOfUserMapper.selectIdByMap(idAndBorrowMap);
            for (String borrowOfUserId :borrowOfUserIds) {

                //获取该用户借阅信息的图书id
                String bookId = borrowOfUserMapper.selectBookIdById(borrowOfUserId);

                int gross=bookMapper.selectNumberById(bookId);

                int lend=borrowOfBookMapper.selectLendById(bookId)
                        -dataTool.getThisIdBorrowOfUserLend(borrowOfUserId);
                //                如果该图书id的lend为0则直接删除该条书籍借阅信息
                if (lend<=0){
                    borrowOfBookMapper.deleteBorrowById(bookId);
                } else {
                    //否则修正书籍借阅信息
                    int stock = gross-lend;
                    Map<String,Object> map = new HashMap();
                    map.put("id",bookId);
                    map.put("stock",stock);
                    map.put("lend",lend);
                    borrowOfBookMapper.updateBorrowByMap(map);

                }


                Map<String,Object> idMap = new HashMap<>();
                idMap.put("id",borrowOfUserId);
                borrowOfUserMapper.deleteBorrowByMap(idMap);

            }


            userMapper.deleteUserById(id);
        }
        catch (Exception e){



            throw new ServiceException();

        }

    }

    //搜索框搜索数据
    public Map<String, Object> selectUserByAny(String selectTxt, int selectKindValue, int thisPage){

        //判断搜索条件
        String selectKey="noKindSelectText";
        switch (selectKindValue){
            case 1:
                selectKey="name";
                break;
            case 2:
                selectKey="phoneNumber";
                break;
        }

        //装配要查询的参数
        Map<String,Object> selectUserByAnyMap=new HashMap<>();
        Map<String,Object> countSelectUserByAnyMap=new HashMap<>();
        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(thisPage);
        selectUserByAnyMap.put(selectKey,selectTxt);
        selectUserByAnyMap.put("offset",pageOffsetRowsMap.get("offset"));
        selectUserByAnyMap.put("rows", pageOffsetRowsMap.get("rows"));

        countSelectUserByAnyMap.put(selectKey,selectTxt);

        //判断传入参数是否合理
        int dataNumber=userMapper.countSelectUserAny(countSelectUserByAnyMap);
        int maxPage=getPageInputOrMaxPage.getMaxPage(dataNumber);
        if (thisPage>maxPage){
            return null;
        }
        //装配翻页插件
        List<User> users=userMapper.selectUserByAny(selectUserByAnyMap);

        Map<String,Object> selectUserByAnyOrPageInputMap=new HashMap<>();


        PageInput pageInput = getPageInputOrMaxPage.getPageInput(thisPage, dataNumber);
        selectUserByAnyOrPageInputMap.put("sUsers",users);
        List<User> sUsers = (List<User>)selectUserByAnyOrPageInputMap.get("sUsers");

        selectUserByAnyOrPageInputMap.put("pageInput",pageInput);
        return selectUserByAnyOrPageInputMap;
    }

}


