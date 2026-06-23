package com.tallerwebi.config.interceptores;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class InterceptorLogin implements HandlerInterceptor {

  //esta clase intercepta al usuario si no esta logeado
  @Override
  public boolean preHandle(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler
  ) throws Exception {
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return false;
    }

    return true;
  }
}
