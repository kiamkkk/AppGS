/**
 * mysql中表的实体映射类.
 * 以DO结尾.
 *
 * @author Phak
 * @since 2023/5/5-23:15
 */
//todo  //作废   不要在getter、setter里加密解密，先自己根据业务判断加密解密把，后面再想办法
//todo 现在有个新想法：DO中在setter里加密，因为这相当于向数据库中存入值
//                  DO中数据只有当需要时才在外部解密，不要在getter解密，因为这相当于从数据库中取出值
package com.gseek.gs.pojo.data;