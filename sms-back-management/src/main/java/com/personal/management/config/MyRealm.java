package com.personal.management.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.PostConstruct;

/**
 * @ClassName MyRealm
 * @Author liupanpan
 * @Date 2026/1/26
 * @Description
 */
public class MyRealm extends AuthorizingRealm {

    /*@PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();

        matcher.setHashAlgorithmName("MD5"); // 加密算法
        matcher.setHashIterations(1024); // 散列次数
//        matcher.setStoredCredentialsHexEncoded(true); // 是否存储为16进制
        super.setCredentialsMatcher(matcher);
    }*/

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 授权
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 认证
        String username = (String) authenticationToken.getPrincipal(); // 用户传过来的用户名
//        String password = (String) authenticationToken.getCredentials(); // shiro自动进行密码校验，告诉shiro密码加密方式以及盐即可
        // 根据用户名查询数据库，获取密码和盐
        // todo 先写死
        if (StringUtils.isBlank(username) ||!"admin".equals(username)) {
            return null;
        }
        String password = "b39dc5da02d002e6ac581e5bb929d2e5";
        String salt = "09a8424ed5bf4373af6530fec2b29c0f";
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, ByteSource.Util.bytes(salt), getName());
        return authenticationInfo;
    }

    /* 配置密码匹配器 用于密码加密 */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();

        matcher.setHashAlgorithmName("MD5"); // 加密算法
        matcher.setHashIterations(1024); // 散列次数
//        matcher.setStoredCredentialsHexEncoded(true); // 是否存储为16进制
//        this.setCredentialsMatcher(matcher); // 会导致栈溢出，无限调用自身
        super.setCredentialsMatcher(matcher);
    }
}
