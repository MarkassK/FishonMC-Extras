package io.github.markassk.fishonmcextras.util;

import com.google.gson.JsonArray;
import com.jagrosh.discordipc.entities.ActivityType;
import com.jagrosh.discordipc.entities.PartyPrivacy;
import com.jagrosh.discordipc.entities.RichPresence;

public class ExtendedRichPresence extends RichPresence {
    public ExtendedRichPresence(ActivityType activityType, String state, String details, long startTimestamp, long endTimestamp, String largeImageKey, String largeImageText, String smallImageKey, String smallImageText, String partyId, int partySize, int partyMax, PartyPrivacy partyPrivacy, String matchSecret, String joinSecret, String spectateSecret, JsonArray buttons, boolean instance) {
        super(activityType, state, details, startTimestamp, endTimestamp, largeImageKey, largeImageText, smallImageKey, smallImageText, partyId, partySize, partyMax, partyPrivacy, matchSecret, joinSecret, spectateSecret, buttons, instance);
    }

    public static class ExtendedBuilder {
        private ActivityType activityType;
        private String state;
        private String details;
        private long startTimestamp;
        private long endTimestamp;
        private String largeImageKey;
        private String largeImageText;
        private String smallImageKey;
        private String smallImageText;
        private String partyId;
        private int partySize;
        private int partyMax;
        private PartyPrivacy partyPrivacy;
        private String matchSecret;
        private String joinSecret;
        private String spectateSecret;
        private JsonArray buttons;
        private boolean instance;

        public ExtendedBuilder setActivity(ActivityType activityType) {
            this.activityType = activityType;
            return this;
        }

        public ExtendedBuilder setState(String state) {
            this.state = state;
            return this;
        }

        public ExtendedBuilder setDetails(String details) {
            this.details = details;
            return this;
        }

        public ExtendedBuilder setStartTimestamp(long startTimestamp) {
            this.startTimestamp = startTimestamp;
            return this;
        }

        public ExtendedBuilder setEndTimestamp(long endTimestamp) {
            this.endTimestamp = endTimestamp;
            return this;
        }

        public ExtendedBuilder setLargeImage(String largeImageKey, String largeImageText) {
            this.largeImageKey = largeImageKey;
            this.largeImageText = largeImageText;
            return this;
        }

        public ExtendedBuilder setLargeImage(String largeImageKey) {
            return setLargeImage(largeImageKey, null);
        }

        public ExtendedBuilder setSmallImage(String smallImageKey, String smallImageText) {
            this.smallImageKey = smallImageKey;
            this.smallImageText = smallImageText;
            return this;
        }

        public ExtendedBuilder setSmallImage(String smallImageKey) {
            return setSmallImage(smallImageKey, null);
        }

        public ExtendedBuilder setParty(String partyId, int partySize, int partyMax) {
            this.partyId = partyId;
            this.partySize = partySize;
            this.partyMax = partyMax;
            return this;
        }

        public ExtendedBuilder setMatchSecret(String matchSecret) {
            this.matchSecret = matchSecret;
            return this;
        }

        public ExtendedBuilder setJoinSecret(String joinSecret) {
            this.joinSecret = joinSecret;
            return this;
        }

        public ExtendedBuilder setSpectateSecret(String spectateSecret) {
            this.spectateSecret = spectateSecret;
            return this;
        }

        public ExtendedBuilder setInstance(boolean instance) {
            this.instance = instance;
            return this;
        }

        public ExtendedRichPresence build() {
            return new ExtendedRichPresence(activityType, state, details, startTimestamp, endTimestamp, largeImageKey, largeImageText, smallImageKey, smallImageText, partyId, partySize, partyMax, partyPrivacy, matchSecret, joinSecret, spectateSecret, buttons, instance);
        }
    }
}
