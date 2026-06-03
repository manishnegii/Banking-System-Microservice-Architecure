//package com.spring.auth_service.JWT;
//
//import com.spring.auth_service.JWT.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//
//@AllArgsConstructor
//@Component
//public class JwtAuthFilter extends OncePerRequestFilter {
//        private final JwtService jwtService;
//    // private final TokenBlacklistRepository blacklistRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        var authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        var token = authHeader.replace("Bearer ", "");
//
//        var jti = jwtService.getJti(token);
//
////         if(blacklistRepository.isBlacklisted(jti)){
////             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////             response.getWriter().write("You have to login first");
////             return;
////         }
//
//        var jwt = jwtService.parseToken(token);
//        if (jwt == null || jwt.isExpired()) {
////            filterChain.doFilter(request, response);
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Invalid or Expired token");
//            return;
//        }
//
//        var role = jwt.getRole();
//
//        var authentication = new UsernamePasswordAuthenticationToken(
//                jwt.getUserId(),
//                null,
//                List.of(new SimpleGrantedAuthority("ROLE_" + jwt.getRole()))
//        );
//        authentication.setDetails(
//                new WebAuthenticationDetailsSource().buildDetails(request)
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
////        SecurityContext.setRole(Role.valueOf(String.valueOf(role)));
//
//        filterChain.doFilter(request, response);
//
//    }
//}
