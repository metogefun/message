package cn.finegames.message.gateway.domain.message;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * message repository
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Repository
public interface MessageRepository {

    /**
     * 根据逐渐删除短信
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入短信信息
     *
     * @param record
     * @return
     */
    int insertSelective(Message record);

    /**
     * 根据主键获取短息信息
     *
     * @param id
     * @return
     */
    Message selectByPrimaryKey(Long id);

    /**
     * 根据逐渐修改短息信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Message record);

    /**
     * 根据task id获取短息信息
     *
     * @param thirdPartyUniqueId
     * @return
     */
    Message selectByThirdPartyUniqueId(String thirdPartyUniqueId);

    /**
     * 修改消息的本地状态
     *
     * @param localStatus
     * @param messageId
     * @param sourceLocalStatus
     * @param modifyTime
     * @return
     */
    int updateLocalStatus(@Param("localStatus") MessageLocalStatusEnum localStatus,
                          @Param("messageId") long messageId,
                          @Param("sourceLocalStatus") MessageLocalStatusEnum sourceLocalStatus,
                          @Param("modifyTime") Date modifyTime);
}
