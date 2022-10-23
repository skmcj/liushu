<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>邮件验证码</title>
</head>

<body>
    <div>
        <div style="width: 100%; margin-bottom: 24px; position: relative;">
            <div
                style="width: 775px; max-width: 95%; margin: 0 auto; background-color: #f8fbf8; border-radius: 6px; display: flex; flex-direction: column; align-items: center; overflow: hidden;">
                <div class="logo-box"
                    style="position: relative; width: 100%; height: 196px; background-color: #83ccd2; display: flex; align-items: center; justify-content: center; overflow: hidden;">
                    <div
                        style="width: 180px; height: 180px; display: flex; align-items: center; justify-content: center;">
                        <img style="width: 100%;" src="https://s3.bmp.ovh/imgs/2022/10/09/5df2bf8d18be173e.png"
                            alt="logo" />
                    </div>
                    <div id="waves"
                        style="width: 100%; position: absolute; bottom: -0.6875rem; -webkit-tap-highlight-color: transparent;">
                        <img style="width: 100%;" src='https://s3.bmp.ovh/imgs/2022/10/09/838136dac0da1aab.gif'
                            alt="waves" />
                    </div>
                </div>
                <div
                    style="position: relative; width: 86%; display: flex; flex-direction: column; align-items: center; margin-bottom: 36px; background-color: #fff; box-shadow: 0 1px 4px rgba(0, 0, 0, 0.27), 0 0 40px rgba(0, 0, 0, 0.06) inset;">
                    <div style="width: 100%; height: 5px; background-color: #83ccd2;"></div>
                    <div
                        style="width: 100%; font-size: 16px; color: #666; line-height: 24px; box-sizing: border-box; padding: 18px 24px;">${cause}</div>
                    <div style="font-size: 18px; margin-top: 12px; color: #333;">您的审核状态为</div>
                    <div style="letter-spacing: 5px; font-size: 27px; font-weight: bold; color: #83ccd2; margin: 16px;">
                        <#if result  == "0">
                            不通过
                        <#else>
                            通过
                        </#if>
                    </div>
                    <div style="box-sizing: border-box; padding: 12px 24px; display: flex; align-items: center;">
                        <div
                            style="width: 18px; height: 18px; display: flex; align-items: center; justify-content: center; margin-right: 8px;">
                            <img style="width: 100%; height: 100%;"
                                src="https://s3.bmp.ovh/imgs/2022/10/09/58e5d2e58bb64ea4.png" alt="tip" />
                        </div>
                        <span style="font-size: 14px; color: #b2b6b6;">
                            <#if result  == "0">
                                很抱歉，您审核资料审核不通过。如对审核结果有异议，可编辑申述邮件致liushumail@liushu.com
                            <#else>
                                恭喜您，成功入驻流书网-图书外卖，现在，您可以使用之前注册的邮箱、密码登录后台管理您的店铺了
                            </#if>
                        </span>
                    </div>
                    <div style="margin-top: 18px; width: 100%; box-sizing: border-box; padding: 18px 24px;">
                        <div style="font-size: 14px; color: #505050; line-height: 27px;">此致：</div>
                        <div style="font-size: 14px; color: #333; font-weight: bold;">流书网团队</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>

</html>