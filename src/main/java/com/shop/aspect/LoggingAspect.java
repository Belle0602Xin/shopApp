package com.shop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.shop.service..*(..)) || execution(* com.shop.controller..*(..))")
    public void applicationPackagePointcut() {
    }

    @Around("applicationPackagePointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        logger.info("Entering method: {}.{}() with arguments: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            logger.info("Exiting method: {}.{}() executed in {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    executionTime);

            return result;
        } catch (Exception ex) {
            long executionTime = System.currentTimeMillis() - startTime;

            logger.error("Method exception: {}.{}() executed in {} ms with exception: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    executionTime,
                    ex.getMessage());

            throw ex;
        }
    }

    @Before("execution(* com.shop.service.OrderService.createOrder(..))")
    public void logBeforeOrderCreation(JoinPoint joinPoint) {
        logger.info("Order creation started, parameters: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* com.shop.service.OrderService.createOrder(..))", returning = "result")
    public void logAfterOrderCreation(JoinPoint joinPoint, Object result) {
        logger.info("Order created successfully, Order ID: {}",
                result != null ? ((com.shop.entity.Order) result).getId() : "null");
    }

    @Before("execution(* com.shop.service.ProductService.reduceInventory(..))")
    public void logBeforeInventoryReduction(JoinPoint joinPoint) {
        logger.info("Inventory reduction started, Product ID: {}, Quantity: {}",
                joinPoint.getArgs()[0], joinPoint.getArgs()[1]);
    }

    @AfterReturning("execution(* com.shop.service.ProductService.reduceInventory(..))")
    public void logAfterInventoryReduction(JoinPoint joinPoint) {
        logger.info("Inventory reduction completed, Product ID: {}, Quantity: {}",
                joinPoint.getArgs()[0], joinPoint.getArgs()[1]);
    }

    @Before("execution(* com.shop.service.UserService.authenticateUser(..))")
    public void logBeforeAuthentication(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof com.shop.dto.LoginDto) {
            com.shop.dto.LoginDto loginDto = (com.shop.dto.LoginDto) args[0];
            logger.info("User login attempt, username/email: {}", loginDto.getUsernameOrEmail());
        }
    }

    @AfterReturning(pointcut = "execution(* com.shop.service.UserService.authenticateUser(..))", returning = "result")
    public void logAfterSuccessfulAuthentication(JoinPoint joinPoint, Object result) {
        if (result instanceof com.shop.entity.User) {
            com.shop.entity.User user = (com.shop.entity.User) result;
            logger.info("User login successful, User ID: {}, Username: {}", user.getId(), user.getUsername());
        }
    }

    @AfterThrowing(pointcut = "execution(* com.shop.service.UserService.authenticateUser(..))", throwing = "ex")
    public void logAfterFailedAuthentication(JoinPoint joinPoint, Exception ex) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof com.shop.dto.LoginDto) {
            com.shop.dto.LoginDto loginDto = (com.shop.dto.LoginDto) args[0];
            logger.warn("User login failed, username/email: {}, Error: {}",
                    loginDto.getUsernameOrEmail(), ex.getMessage());
        }
    }
}