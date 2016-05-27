package com.ly.common.utils;

import com.ly.flower.base.DataStructure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2016/4/5.
 */
public class TimeUtils {

    /**
     * 秒转时间
     * @param nTime
     * @return
     */
    public static String secondsToTimeSting(long nTime)
    {
        if (nTime <= 0) {
            return "0" + DataStructure.strTime[0];
        }
        String strResult = "";

        int i = 0;
        while (nTime > 0 ) {
            if (i < 2) {
                strResult = String.valueOf(nTime % 60) + DataStructure.strTime[i] +strResult;
                nTime = nTime / 60;
            }else if (i== 2) {
                strResult = String.valueOf(nTime % 24) + DataStructure.strTime[i] +strResult;
                nTime = nTime / 24;
            }else {
                strResult = String.valueOf(nTime) + DataStructure.strTime[i] +strResult;
                break;
            }

            i++;
        }
        return strResult;
    }

    /**
     * 秒转时间
     * @param nTime
     * @return
     */
    public static String secondsToTimeStr(long nTime)
    {
        if (nTime <= 0) {
            return "00:00:00";
        }
        String strResult = "";
        int count = (int)(nTime % 60);
        String strCount = String.valueOf(nTime % 60);
        if (count < 10)
        {
            strCount = "0" + strCount;
        }
        strResult = ":" + strCount + strResult;
        nTime = nTime / 60;
        count = (int)(nTime % 60);
        strCount = String.valueOf(nTime % 60);
        if (count < 10)
        {
            strCount = "0" + strCount;
        }
        strResult = ":" + strCount + strResult;
        nTime = nTime / 24;
        count = (int)(nTime );
        strCount = String.valueOf(count);
        if (count < 10)
        {
            strCount = "0" + strCount;
        }
        strResult =  strCount + strResult;


        return strResult;
    }


    public static String getDateFromTime(String strTime)
    {
        int nPos = strTime.lastIndexOf(" ");
        return strTime.substring(0, nPos);
    }

    public static String getCurrentTime()
    {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate   =   new   Date(System.currentTimeMillis());
        return sfd.format(curDate);
    }

    public static int getWeekDayByDate(String sdate)
    {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        if (day == 1)
            return 7;
        else
            return day - 1;
    }

    public static String compareWithCurrentTime(String strTime) {
        String timeAfterChange = "";
        final int minute = 60*1000;   //1分钟
        final int hour = minute * 60; //1小时
        final int day = hour * 24;    //1天
        final int week = day * 7;     //1周
        final int month = week * 30;  //1月

        //strTime的格式: yyyy-MM-dd HH:mm:ss
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到指定模范的时间 (讲道理，curDate 会比 Mytime 大)
        Date Mytime = null;
        try {
            Mytime = sdf.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date curDate = new Date(System.currentTimeMillis());

        //比较
        long nTime = Math.abs(curDate.getTime() - Mytime.getTime());
        if((nTime / minute) < 1) {
            //刚刚
            timeAfterChange = "刚刚";
        } else if((nTime / hour) < 1) {
            //*分钟前
            timeAfterChange = nTime / minute + "分钟前";
        } else if((nTime / day) < 1) {
            //*小时前
            timeAfterChange = nTime / hour + "小时前";
        } else if((nTime / week) < 1) {
            //*天前
            timeAfterChange = nTime / day + "天前";
        } else if((nTime / month) < 1) {
            //*星期前
            timeAfterChange = nTime / week + "星期前";
        } else {
            //yyyy-MM-dd HH:mm:ss
            timeAfterChange = strTime;
        }

        if(timeAfterChange.equals("")) {
            timeAfterChange = strTime;
        }

        System.out.println(1);
        return timeAfterChange;
    }
}
