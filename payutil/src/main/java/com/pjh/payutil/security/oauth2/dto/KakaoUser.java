package com.pjh.payutil.security.oauth2.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.pjh.payutil.domain.Role;

public class KakaoUser implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final KakaoAttributes KakaoAttributes;

    public KakaoUser(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
        this.KakaoAttributes = new KakaoAttributes(oAuth2User.getAttributes());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return Role.ROLE_FREE.name();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return KakaoAttributes.getProviderId();
    }

    public String getNickname() {
        return KakaoAttributes.getNickname();
    }

    public String getProfileImageURL() {
        return KakaoAttributes.getProfileImageURL();
    }

    private class KakaoAttributes {

        private static final String ID = "id";
        private static final String KAKAO_ACCOUNT = "kakao_account";
        private static final String PROFILE = "profile";
        private static final String NICKNAME = "nickname";
        private static final String PROFILE_IMAGE_URL = "profile_image_url";

        private final Map<String, Object> attributes;

        public KakaoAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public String getProviderId() {
            return String.valueOf(attributes.get(ID));
        }

        public String getNickname() {
            Map<String, Object> profile = getProfile();
            return (String) profile.get(NICKNAME);
        }

        public String getProfileImageURL() {
            Map<String, Object> profile = getProfile();
            return (String) profile.get(PROFILE_IMAGE_URL);
        }

        private Map<String, Object> getProfile() {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get(KAKAO_ACCOUNT);
            return (Map<String, Object>) kakaoAccount.get(PROFILE);
        }

    }

}
