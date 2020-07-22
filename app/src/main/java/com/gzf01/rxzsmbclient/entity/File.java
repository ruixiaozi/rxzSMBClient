package com.gzf01.rxzsmbclient.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Title: File 类 <br/>
 * Description: 文件属性 <br/>
 * CreateTime: 2020/7/22 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class File implements Serializable {

    private int index;

    private String name;

    private Date cTime;

    private boolean dir;

}
