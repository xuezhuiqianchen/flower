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
}
