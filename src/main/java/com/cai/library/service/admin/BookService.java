package com.cai.library.service.admin;

import com.cai.library.dao.admin.Book;
import com.cai.library.dao.admin.PageInput;
import com.cai.library.dataCreate.pojo.CreateUuid;
import com.cai.library.exception.ServiceException;
import com.cai.library.mapper.admin.BookMapper;
import com.cai.library.mapper.admin.BorrowOfBookMapper;
import com.cai.library.mapper.admin.BorrowOfUserMapper;
import com.cai.library.pojo.GetPageInputOrMaxPage;
import com.cai.library.pojo.GetPageOffsetRows;
import com.cai.library.pojo.GetSnowId;
import com.cai.library.type.SnowIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    @Autowired
    BookMapper bookMapper;

    @Autowired
    GetSnowId getSnowId;

    @Autowired
    BorrowOfBookMapper borrowOfBookMapper;

    @Autowired
    BorrowOfUserMapper borrowOfUserMapper;


    //mysqlclass表相关的服务层
    @Autowired
    KindService kindService;

    @Autowired
    FileUrlService fileUrlService;


    //装配获取mysql的limit字段参数的类
    @Autowired
    GetPageOffsetRows getPageOffsetRows;


    //装配构建uuid的类
    @Autowired
    CreateUuid createUuid;

    @Autowired
    GetPageInputOrMaxPage getPageInputOrMaxPage;


    int maxPage;


    public List<Book> selectAllBook(){




            return  bookMapper.selectAll();

    }

    //根据页数获取该页数据和翻页组件的
    //map:{该页数据，翻页组件}
    public Map<String,Object> selectBookByPage(int page){
        Map<String,Object> thisPageDataAndPageInputMap=new HashMap<>();
        //判断传入的页数是否合法或者搜索结果是否为空
        int dataNumber= bookMapper.getDataNumber();
        int maxPage=getPageInputOrMaxPage.getMaxPage(dataNumber);
        if(page<1 || page>maxPage || dataNumber==0){
            return null;
        }


        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(page);

        //该页数据和翻页组件的获取
        thisPageDataAndPageInputMap.put("thisPageData",bookMapper.selectByPage(pageOffsetRowsMap));
        thisPageDataAndPageInputMap.put("PageInput",getPageInputOrMaxPage.getPageInput(page,dataNumber));

            return thisPageDataAndPageInputMap ;
    }



    //获取修改数据
    public Book getEditInfo(String id){


        return bookMapper.selectBookById(id);

    }

    //修改数据

    @Transactional
    public void upDateBook(Book book,MultipartFile file) {



        try {

            //获取原先书籍总量信息
            int originalGross = bookMapper.selectNumberById(book.getId());


            //如果修改了书籍总量信息且该图书id的书籍借阅信息存在，那就修正拥有该图书id的库存信息
            if (borrowOfBookMapper.countBorrowById(book.getId()) == 1 && originalGross!=book.getNumber()){



                int lend = borrowOfBookMapper.selectLendById(book.getId());
                int gross = book.getNumber();
                int stock = gross-lend;
                Map<String,Object> map = new HashMap();
                map.put("id", book.getId());
                map.put("gross",gross);
                map.put("stock",stock);
                map.put("lend",lend);

                borrowOfBookMapper.updateBorrowByMap(map);
            }


            //修改图片
            if (file!=null){
                Map<Boolean, String> resultMap = fileUrlService.saveImg(file, book.getId());
                if (resultMap.containsKey(true)){
                    //获取类型名
                    String imgFormat = resultMap.get(true).split("\\.")[1];
                    book.setImgFormat(imgFormat);
                }
            }
            bookMapper.updateBook(book);

        }catch (Exception e){



            throw new ServiceException("修改数据失败");
        }





    }

    //添加数据
    @Transactional
    public void addBook(Book book, MultipartFile file) throws FileNotFoundException {
        try {
            String bookId=String.valueOf(getSnowId.setSonwIdType(SnowIdType.BOOK).nextId());
            //图片添加失败
            Map<Boolean, String> requltMap = fileUrlService.saveImg(file, bookId);

            if (requltMap.containsKey(false)) {
                throw new FileNotFoundException(requltMap.get(false));
            }

            String imgFormat = requltMap.get(true).split("\\.")[1];

            book.setId(bookId);
            book.setImgFormat(imgFormat);

            bookMapper.addBook(book);

        }

        catch (FileNotFoundException e){

        }

        catch (Exception e){



            throw new ServiceException();
        }

    }

    //删除数据
    @Transactional
    public int deleteBook(String id){


        //删除用户借阅信息U_id=id的数据
        borrowOfUserMapper.deleteBorrowByBookId(id);

        //删除书籍借阅信息id=id的数据
        borrowOfBookMapper.deleteBorrowById(id);

        //删除图片
        String imgFormat = bookMapper.selectFormatById(id);
        if ( imgFormat!=null|| !imgFormat.equals("")){
            ApplicationHome homePath = new ApplicationHome(getClass());
            File jarF = homePath.getSource();
            //在jar包目录下生成一个upload文件夹用来存储上传的图片
            String parent = jarF.getParentFile().toString() + "/classes/static/images/";
            String file=parent+id+"."+imgFormat;
            File filename = new File(file);
            if (filename.exists()){
                System.out.println(file);
                filename.delete();
            }
        }


        return bookMapper.deleteBook(id);
    }



    //搜索框搜索数据
    public Map selectBookByAny(String selectTxt, int selectKindValue, int thisPage){

        //判断搜索条件
        String selectKey="noKindSelectText";
        switch (selectKindValue){
            case 1:
                selectKey="title";
                break;
            case 2:
                selectKey="author";
                break;
        }

        //装配要查询的参数
        Map<String,Object> selectBookByAnyMap=new HashMap<>();
        Map<String,Object> countSelectBookByAnyMap=new HashMap<>();
        Map pageOffsetRowsMap = getPageOffsetRows.getPageOffsetRowsMap(thisPage);
        selectBookByAnyMap.put(selectKey,selectTxt);
        selectBookByAnyMap.put("offset",pageOffsetRowsMap.get("offset"));
        selectBookByAnyMap.put("rows", pageOffsetRowsMap.get("rows"));

        countSelectBookByAnyMap.put(selectKey,selectTxt);

        //判断传入参数是否合理
        int dataNumber=bookMapper.countSelectBookAny(countSelectBookByAnyMap);
        int maxPage=getPageInputOrMaxPage.getMaxPage(dataNumber);
        if (thisPage>maxPage){
            return null;
        }
        //装配翻页插件
        List<Book> books=bookMapper.selectBookByAny(selectBookByAnyMap);

        Map<String,Object> selectBookByAnyOrPageInputMap=new HashMap<>();


        PageInput pageInput = getPageInputOrMaxPage.getPageInput(thisPage, dataNumber);
        selectBookByAnyOrPageInputMap.put("sbooks",books);
        selectBookByAnyOrPageInputMap.put("pageInput",pageInput);
        return selectBookByAnyOrPageInputMap;
    }



}
