package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.skypro.homework.utils.PatternUtils.PATTERN_CORS_HEADER;

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * метод одностороннего шифрования пароля
     * @param httpServletRequest - HTTP запрос пользователя
     * @param httpServletResponse - HTTP ответ сервера
     * @param filterChain - цепочка фильтров запроса
     * @throws ServletException - обработка возможных ошибок при взаимодействии доменов
     * addHeader - CORS, который фильтрует домены
     */

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader(PATTERN_CORS_HEADER, "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
