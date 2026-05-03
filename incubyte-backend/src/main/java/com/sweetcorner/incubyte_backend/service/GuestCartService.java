package com.sweetcorner.incubyte_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sweetcorner.incubyte_backend.entity.CartItem;

@Service
public class GuestCartService {
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final String CART_PREFIX="cart:guest";

    //sessionId A unique string from the frontend (like a UUID)

    public void addItemToGuestCart(String sessionId,CartItem item){
        String key=CART_PREFIX+sessionId;

        // 1. Get the existing cart from Redis, or create a new list if it's empty
        List<CartItem> cart=getGuestCart(sessionId);
        if(cart==null){
            cart=new ArrayList<>();
        }

        //add item to the cart
        cart.add(item);

        // save the uploaded list back to the redis
        redisTemplate.opsForValue().set(key, cart);

        //set it to automatically delete after 24 hours if they don't buy anything
        redisTemplate.expire(key,24,TimeUnit.HOURS);
    }

        @SuppressWarnings("unchecked")
        public List<CartItem> getGuestCart(String sessionId){
            String key=CART_PREFIX+sessionId;
            Object cartData=redisTemplate.opsForValue().get(key);

            if(cartData!=null){
                return (List<CartItem>)cartData;
                //cast the JSOn data back into the Java List
            }
            return new ArrayList<>();
        }

        //deletes cart from the redis
        // when the user logs in then this information moves to the postgresSQl 
        // so we don't needs to store in then Redis
        //or they checkout

        public void clearGuestCart(String sessionId){
            String key=CART_PREFIX+sessionId;
            redisTemplate.delete(key);
        }
    
}
