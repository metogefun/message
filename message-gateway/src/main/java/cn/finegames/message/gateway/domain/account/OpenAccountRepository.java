package cn.finegames.message.gateway.domain.account;

import org.springframework.stereotype.Repository;

/**
 * open account repository
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Repository
public interface OpenAccountRepository {

    /**
     * 根据逐渐查询
     *
     * @param id
     * @return
     */
    OpenAccount selectByPrimaryKey(int id);

    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(int id);

    /**
     * 插入账号
     *
     * @param account
     * @return
     */
    int insertSelective(OpenAccount account);

    /**
     * 根据主键修改账号
     *
     * @param account
     * @return
     */
    int updateByPrimaryKeySelective(OpenAccount account);


    /**
     * 根据app id查询账号
     *
     * @param appId
     * @return
     */
    OpenAccount selectByAppId(String appId);
}
