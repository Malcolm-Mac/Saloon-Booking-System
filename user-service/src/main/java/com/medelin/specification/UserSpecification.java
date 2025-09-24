package com.medelin.specification;

import com.medelin.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification
{
    public static Specification<User> nameContains(String name)
    {
        return (root, query, cb) ->{
            if(name==null || name.trim().isEmpty())
            {
                return null;
            }
            return cb.like(cb.lower(root.get("fullName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<User> emailContains(String email)
    {
        return (root, query, cb) ->
        {
            if(email==null || email.trim().isEmpty())
            {
                return null;
            }
            return cb.like(cb.lower(root.get("email")), email.toLowerCase() + "%");
        };
    }
}
