package br.com.welike.instagram.converter;

import br.com.welike.instagram.Converter;
import br.com.welike.instagram.model.Influencer;
import me.postaddict.instagram.scraper.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountInfluencerConverter implements Converter<Account, Influencer> {

    @Override
    public Influencer encode(Account account) {
        Influencer Influencer = new Influencer();

        Influencer.setInstagramId(account.getId());
        Influencer.setBio(account.getBiography());
        Influencer.setFollowedBy(account.getFollowedBy());
        Influencer.setFollows(account.getFollows());
        Influencer.setFullName(account.getFullName());
        Influencer.setIsPrivate(account.getIsPrivate());
        Influencer.setUserName(account.getUsername());
        Influencer.setProfilePicture(account.getProfilePicUrl());

        return Influencer;
    }

    @Override
    public Account decode(Influencer input) {
        return null;
    }
}
