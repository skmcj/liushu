package top.skmcj.liushu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.entity.Employee;

import java.util.Date;
import java.util.Map;

/**
 * JWt相关操作工具类
 */
@Component
public class JwtUtil {
    // JWT签名秘钥
    private static String SECRET;

    // JWT默认有效时间
    private static long EXPIRES;

    /**
     * token默认的块数
     */
    private static final Integer DEFAULT_SIZE = 3;

    @Value("${liushu.jwt.secret}")
    public void setSECRET(String SECRET) {
        JwtUtil.SECRET = SECRET;
    }

    @Value("${liushu.jwt.expires}")
    public void setEXPIRES(long EXPIRES) {
        JwtUtil.EXPIRES = EXPIRES;
    }

    /**
     * 获取员工登录的token
     * @param employee
     * @return
     */
    public static String getToken(Employee employee) throws JWTDecodeException {
        Date expiresDate = new Date(System.currentTimeMillis() + EXPIRES * 1000);
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String token = null;
        token = JWT.create()
                .withIssuer("liushu")
                .withClaim("type", "employee")
                .withClaim("id", employee.getId())
                .withClaim("competence", employee.getCompetence())
                .withClaim("storeId", employee.getStoreId())
                .withIssuedAt(new Date())
                .withExpiresAt(expiresDate)
                .sign(algorithm);

        return encodeToken(token);
    }


    /**
     * 校验token
     * @param token
     * @return
     */
    public static Employee verifyTokenOfEmployee(String token) throws Exception {
        //如果token无效
        if (token == null || "".equals(token)) {
            throw new JWTDecodeException("无效的token！");
        }
        //解析token
        String dToken = decodeToken(token);
        //创建返回结果
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(dToken);
        // System.out.println("type => " + verify.getClaim("type").asString());
        if(!verify.getClaim("type").asString().equals("employee")) {
            throw new JWTDecodeException("无法解析为员工对象");
        }
        Employee employee = new Employee();
        employee.setId(verify.getClaim("id").asLong());
        employee.setCompetence(verify.getClaim("competence").asInt());
        employee.setStoreId(verify.getClaim("storeId").asLong());
        return employee;
    }

    /**
     * 生成token
     * @param map     数据正文
     * @param expires 过期时间，单位(秒)
     */
    public static String getToken(Map<String, String> map, Integer expires) throws JWTDecodeException {
        //设置过期时间
        Date expiresDate = new Date(System.currentTimeMillis() + expires * 1000);

        //创建jwt builder对象
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        //指定过期时间
        String token = builder.withExpiresAt(expiresDate)
                //设置加密方式
                .sign(Algorithm.HMAC256(SECRET));
        //返回tokean
        return encodeToken(token);
    }

    /**
     * 解析token
     * @param token 输入混淆payload后的token
     */
    public static DecodedJWT verifyToken(String token) throws Exception {
        //如果token无效
        if (token == null || "".equals(token)) {
            throw new JWTDecodeException("无效的token！");
        }
        //解析token
        String dToken = decodeToken(token);
        //创建返回结果
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(dToken);
    }

    /**
     * 编码混淆token
     * 已达到加密payload的目的
     * @param token
     * @return
     */
    private static String encodeToken(String token) throws JWTDecodeException {
        String[] split = token.split("\\.");
        if(split.length != DEFAULT_SIZE) {
            throw new JWTDecodeException("token不正确");
        }
        //取出payload
        String payload = split[1];
        //获取长度
        int length = payload.length() / 2;
        //指定截取点
        int index = payload.length() % 2 != 0 ? length + 1 : length;
        //混淆处理后的token
        return split[0] + "." + reverseStr(payload, index) + "." + split[2];
    }

    /**
     * 解析混淆后的token
     * @param token
     * @return 真实的token
     * @throws Exception
     */
    private static String decodeToken(String token) throws JWTDecodeException {
        String[] split = token.split("\\.");
        //如果token不符合规范
        if (split.length != DEFAULT_SIZE) {
            throw new JWTDecodeException("token不正确");
        }
        //取出payload
        String payload = split[1];
        //返回解析后的token
        return split[0] + "." + reverseStr(payload, payload.length() / 2) + "." + split[2];
    }

    /**
     * 混淆字符串
     * @param content
     * @param index
     * @return
     */
    private static String reverseStr(String content, Integer index) {
        return content.substring(index) + content.substring(0, index);
    }
}
