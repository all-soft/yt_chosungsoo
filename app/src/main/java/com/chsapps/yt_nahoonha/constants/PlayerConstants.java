package com.chsapps.yt_nahoonha.constants;

public class PlayerConstants {

    public static final int PLAYER_TYPE_FLOATING = 0;
    public static final int PLAYER_TYPE_FLOATING_MINIMUM = 1;
    public static final int PLAYER_TYPE_FLOATING_ETIRE_WIDTH = 2;
    public static final int PLAYER_TYPE_IN_FRAGMENT = 3;

    public  static  int playbackQuality = 3;

    private static String strPlaybackQuality = "large";
    public static String getPlaybackQuality() {
        if(playbackQuality == 0){
            strPlaybackQuality = "auto";
        }
        else if (playbackQuality == 1){
            strPlaybackQuality = "hd1080";
        }
        else if (playbackQuality == 2){
            strPlaybackQuality = "hd720";
        }
        else if (playbackQuality == 3){
            strPlaybackQuality = "large";
        }
        else if (playbackQuality == 4){
            strPlaybackQuality = "medium";
        }
        else if (playbackQuality == 5){
            strPlaybackQuality = "small";
        }
        else{
            strPlaybackQuality = "tiny";
        }
        return strPlaybackQuality;
    }


    //Actions
    public interface ACTION {
        String ACTION_PLAY_PREV_SONG = "com.chsapps.yt_nahoonha.action.prev";
        String ACTION_PLAY_OR_PAUSE = "com.chsapps.yt_nahoonha.action.play";
        String ACTION_PLAY_NEXT_SONG = "com.chsapps.yt_nahoonha.action.next";
        String ACTION_START_FORGROUNT_WEB = "com.chsapps.yt_nahoonha.action.playingweb";
        String ACTION_STOP_FORGROUNT_WEB = "com.chsapps.yt_nahoonha.action.stopplayingweb";
        String ACTION_SET_YOUTUBE_PLAYER_TYPE = "com.chsapps.yt_nahoonha.action.player_type";
        String ACTION_CLOSE_PLAYER = "com.chsapps.yt_nahoonha.action.close_player";
    }

    //Notification Id
    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

}
