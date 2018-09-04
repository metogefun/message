package cn.finegames.message.gateway.interfaces.message;

import cn.finegames.commons.util.BaseResp;
import cn.finegames.message.gateway.interfaces.message.facade.MessageServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 短信controller
 *
 * @author wang zhaohui
 * @since 1.0
 */
@RestController
public class MessageController {

    @Autowired
    private MessageServiceFacade messageServiceFacade;

    /**
     * 短信
     *
     * @param smsCommand
     * @return
     */
    @PostMapping("/message/sms")
    public BaseResp saveSms(@Valid SmsCommand smsCommand) {
        messageServiceFacade.save(smsCommand);
        return BaseResp.SUCCESS;
    }


    @PostMapping("/message/ding")
    public BaseResp saveDing(@Valid DingCommand dingCommand) {
        messageServiceFacade.save(dingCommand);
        return BaseResp.SUCCESS;
    }

}
