package com.vogtec.ibx5.manager;

/**
 * Created by PC on 2017/3/6.
 */

import android.text.TextUtils;

import com.vogtec.ibx5.R;
import com.vogtec.ibx5.dao.IUserInfoDao;
import com.vogtec.ibx5.dao.impl.UserInfoDao;
import com.vogtec.ibx5.domain.User;
import com.vogtec.ibx5.domain.UserInfo;
import com.vogtec.ibx5.exception.EditUserInfoException;
import com.vogtec.utils.utils.ResourceUtils;

/**
 * 用户信息管理者
 */
public class UserInfoManager {
    private static UserInfoManager manager = new UserInfoManager();
    private final IUserInfoDao mInfoDao;

    private UserInfoManager() {
        mInfoDao = new UserInfoDao();
    }

    public static UserInfoManager getInstance() {
        return manager;
    }

    /**
     * 修改用户名
     *
     * @param name
     * @throws EditUserInfoException
     */
    public void modifyName(String name) throws EditUserInfoException {
        if (TextUtils.isEmpty(name)) {
            throw new EditUserInfoException(ResourceUtils.getString(R.string.username_not_null));
        }
        UserInfo info = mInfoDao.findUserByUsername(name);
        if (info != null) {
            throw new EditUserInfoException(ResourceUtils.getString(R.string.username_already_exists));
        }

        User instance = User.getInstance();
        instance.setUsername(name);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfo(instance);

        mInfoDao.insertUserInfo(userInfo);


    }

    /**
     * 修改年龄
     *
     * @param age
     */
    public void modifyAge(String age) throws EditUserInfoException {
        if (TextUtils.isEmpty(age)) {
            throw new EditUserInfoException(ResourceUtils.getString(R.string.age_not_null));
        }
        User instance = User.getInstance();
        instance.setAge(age);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfo(instance);

        mInfoDao.insertUserInfo(userInfo);
    }

    /**
     * 修改体重
     *
     * @param weight
     * @throws EditUserInfoException
     */
    public void modifyWeight(String weight) throws EditUserInfoException {
        if (TextUtils.isEmpty(weight)) {
            throw new EditUserInfoException(ResourceUtils.getString(R.string.weight_not_null));
        }
        User instance = User.getInstance();
        instance.setWeight(weight);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfo(instance);

        mInfoDao.insertUserInfo(userInfo);

    }

    /**
     * 修改身高
     *
     * @param height
     * @throws EditUserInfoException
     */
    public void modifyHeight(String height) throws EditUserInfoException {
        if (TextUtils.isEmpty(height)) {
            throw new EditUserInfoException(ResourceUtils.getString(R.string.height_not_null));
        }
        User instance = User.getInstance();
        instance.setHeight(height);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfo(instance);


        mInfoDao.insertUserInfo(userInfo);
    }

    /**
     * 密码修改
     *
     * @param password
     * @throws EditUserInfoException
     */
    public void modifyPassword(String password) throws EditUserInfoException {
        if (TextUtils.isEmpty(password)) {
            throw new EditUserInfoException(ResourceUtils.getString(R.string.password_not_null));
        }

        String oldPassword = User.getInstance().getPassword();
        if (oldPassword.equals(password)) {
            //The old and new passwords are the same
            throw new EditUserInfoException(ResourceUtils.getString(R.string.old_and_new_password_same));
        }

        User instance = User.getInstance();
        instance.setPassword(password);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserInfo(instance);

        mInfoDao.insertUserInfo(userInfo);

    }
}
