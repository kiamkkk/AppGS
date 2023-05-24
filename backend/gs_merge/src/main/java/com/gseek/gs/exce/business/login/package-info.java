/**
 * 登录组件中抛出的异常.
 * 因为这些异常在filter中被抛出，只被AuthenticationFailureHandler处理，所以要继承自AuthenticationException.
 *
 * @author Phak
 * @since 2023/5/23-18:31
 */
package com.gseek.gs.exce.business.login;