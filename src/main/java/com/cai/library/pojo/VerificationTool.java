package com.cai.library.pojo;

import com.cai.library.dao.admin.BorrowOfUser;
import com.cai.library.mapper.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VerificationTool {
    @Autowired
    BorrowOfUserMapper borrowOfUserMapper;
    @Autowired
    BorrowOfBookMapper borrowOfBookMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    BookMapper bookMapper;

    @Autowired
    LoginMapper loginMapper;

    @Autowired
    KindMapper kindMapper;




    //获取同一id下所有borrow=0之和
    public int actNumberSumByIdWhenBorrwIS0(String id){

        int sum=0;
            Map<String,Object> idAndBorrowMap=new HashMap<>();
            idAndBorrowMap.put("id",id);
            idAndBorrowMap.put("borrow",0);
            List<Integer> actNumberList = borrowOfUserMapper.selectActNumberByIdAndBorrowMap(idAndBorrowMap);
            for (Integer integer : actNumberList) {

                sum+=integer;
            }

            return sum;



    }



    //验证兄弟信息是否存在
    public boolean brotherDataVerification(String id,int borrow){
        //使用位非操作取得兄弟信息的borrow
        int brotherBorrow = ~borrow;
        Map<String,Object> idAndBorrowMap=new HashMap<>();
        idAndBorrowMap.put("id",id);
        idAndBorrowMap.put("borrow",brotherBorrow);
        return borrowOfUserMapper.countBorrowByIdAndBorrwMap(idAndBorrowMap)==1;
    }


    //验证借书还书时间的合理性(还得验证是否有相等时间
    public boolean  dateVerification(String id,Date date,int borrow){



        if (borrow==0){

            //获取同一id下borrow=0的时间
            Map<String,Object> idAndBorrowMapBorrowIs0 = new HashMap();
            idAndBorrowMapBorrowIs0.put("id",id);
            idAndBorrowMapBorrowIs0.put("borrow",0);
            List<Date> bIs0Dates = borrowOfUserMapper.selectDateByIdAndBorrowMap(idAndBorrowMapBorrowIs0);


            ///获取同一id下borrow=1的时间
            Map<String,Object> idAndBorrowMapBorrowIs1 = new HashMap();
            idAndBorrowMapBorrowIs1.put("id",id);
            idAndBorrowMapBorrowIs1.put("borrow",1);
            Date bIs1Date = borrowOfUserMapper.selectDateByIdAndBorrowMap(idAndBorrowMapBorrowIs1).get(0);

            //borow=0的时间不能等于同一id的borrow=0的时间
            for (Date bIs0Date : bIs0Dates) {

                if (date.getTime()==bIs0Date.getTime()){
                    return false;
                }
            }





            if (bIs1Date.getTime()==date.getTime()|| date.before(bIs1Date)) {
                System.out.println("来啦");
                return false;
            }

            return true;
        }else if (borrow==1){

            //获取同一id下borrow=0的时间
            Map<String,Object> idAndBorrowMap = new HashMap();
            idAndBorrowMap.put("id",id);
            idAndBorrowMap.put("borrow",0);
            List<Date> bIs0Dates = borrowOfUserMapper.selectDateByIdAndBorrowMap(idAndBorrowMap);

            //borrw=1的时间不能等于或晚于同一id的borrow=0的时间
            for (Date bIs0Date : bIs0Dates) {
               if ( date.getTime()==bIs0Date.getTime() ||date.after(bIs0Date)) {
                    return false;
                }
            }


            return true;
        }


        return false;
    }

    //验证用户是否存在
    public boolean userIdVerification(String id){

            return userMapper.countSelectUserById(id)==1;


    }

    //验证书籍是否存在
    public boolean bookIdVerification(String id) {

        return bookMapper.countSelectBookById(id) == 1;
    }


    //验证添加信息的行为数量的合理性
    public boolean actNumberVerification(BorrowOfUser borrow){
        boolean b=false;


        //信息词条是借书时
        if(borrow.getBorrow()==1){
            int bookNumber=0;
            try {
                 bookNumber = bookMapper.selectNumberById(borrow.getBookId());
                //验证借书数量是否小于库存
                if (bookNumber<borrow.getActNumber()){
                    return false;
                }

                //验证借书数量是否大于的等于还书总和
                int sum=actNumberSumByIdWhenBorrwIS0(borrow.getId());
                b=borrow.getActNumber()>=sum;

            }catch (Exception e){
                return false;
            }

            //信息词条是还书时
        } else if (borrow.getBorrow()==0) {
            Map<String,Object> idAndBorrowMap=new HashMap<>();
            idAndBorrowMap.put("id",borrow.getId());
            idAndBorrowMap.put("borrow",1);
            int borrowIs1AtcNumber = borrowOfUserMapper.selectActNumberByIdAndBorrowMap(idAndBorrowMap).get(0);
            int sum=0;


                sum=actNumberSumByIdWhenBorrwIS0(borrow.getId())+borrow.getActNumber();


            System.out.println("borrowIs1AtcNumber=" + borrowIs1AtcNumber);
            System.out.println("sum=" + sum);

            b=borrowIs1AtcNumber>=sum;
        }
        return b;

    }

    //验证编辑信息的行为数量合理性
    public boolean actNumberVerification(BorrowOfUser borrow,int increment){
        boolean b=false;


        //信息词条是借书时
        if(borrow.getBorrow()==1){
            int bookNumber=0;
            try {
                bookNumber = bookMapper.selectNumberById(borrow.getBookId());
                //验证借书数量是否小于库存
                if (bookNumber<borrow.getActNumber()){
                    return false;
                }

                //验证借书数量是否大于的等于还书总和
                int sum=actNumberSumByIdWhenBorrwIS0(borrow.getId());
                b=borrow.getActNumber()>=sum;

            }catch (Exception e){
                return false;
            }

            //信息词条是还书时
        } else if (borrow.getBorrow()==0) {
            Map<String,Object> idAndBorrowMap=new HashMap<>();
            idAndBorrowMap.put("id",borrow.getId());
            idAndBorrowMap.put("borrow",1);
            int borrowIs1AtcNumber = borrowOfUserMapper.selectActNumberByIdAndBorrowMap(idAndBorrowMap).get(0);
            int sum=0;


            sum=actNumberSumByIdWhenBorrwIS0(borrow.getId())+increment;




            b=borrowIs1AtcNumber>=sum;
        }
        return b;

    }

    //验证图书借阅信息是否存在
    public boolean borrowOfBookVerification(String id){
        return  borrowOfBookMapper.countBorrowById(id)==1;

    }

    //验证用户借阅信息是否修改了图书id
    public boolean boookIdModifyVerification(String id,String bookId){
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("bookId",bookId);


        return borrowOfUserMapper.selectBookIdById(id).equals(bookId);


    }

    //验证图书总数的合理性
    public  boolean numberOfBookVerification(String bookId,int bookNumber){


        //借出的书的数量
        Integer lend = borrowOfBookMapper.selectLendById(bookId);
        if (lend==null){
            lend=0;
        }




        //图书总量不能比借出的书少
        return bookNumber>=lend;
    }


    //验证是否是管理员
    public boolean adminVerification(String account){
        return loginMapper.countAdminByAccount(account)==1;
    }


    //验证分类是否存在
    public boolean kindVerification(int kindId){

        return kindMapper.countKindById(kindId)==1;
    }

    //验证用户账号是否存在
    public boolean userAccountVerification(String userAccount){
        if (loginMapper.countUserAccount(userAccount)<1){
            return false;
        }
        return true;
    }

}
