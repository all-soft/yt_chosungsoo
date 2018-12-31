package com.chsapps.yt_nahoonha.ui.youtube_player;

public class JavaScript {

    public static String loadVideoScript(String vId){
        return "javascript:player.loadVideoById(\"" + vId + "\");";
    }

    public static String playVideoScript() {
        return "javascript:player.playVideo();";
    }

    public static String pauseVideoScript() {
        return "javascript:player.pauseVideo();";
    }

    public static String onPlayerStateChangeListener() {
        return "javascript:" +
                "player.addEventListener(\"onStateChange\", \"onPlayerStateChange\");"+
                "function onPlayerStateChange(event) {\n" +
                "      window.Interface.showPlayerState(player.getPlayerState());\n" +
                "  }";
    }

    public static String loadPlaylistScript(String pId) {
        return "javascript:player.loadPlaylist({list:\"" + pId + "\"});";
    }

    public static String nextVideo() {
        return "javascript:player.nextVideo()";
    }

    public static String prevVideo() {
        return "javascript:player.previousVideo()";
    }

    public static String getVidUpdateNotiContent() {
        return "javascript:window.Interface.showVID(player.getVideoData()['video_id']);";
    }

    public static String seekTo(float seconds, boolean allowSeekAhead) {
        return "javascript:player.seekTo(" + seconds + "," + allowSeekAhead + ")";
    }

    public static String seekToZero() {
        return "javascript:player.seekTo(0)";
    }

    public static String setLoopPlaylist() {
        return "javascript:player.setLoop(true)";
    }

    public static String unsetLoopPlaylist() {
        return "javascript:player.setLoop(false)";
    }

    public static String replayPlaylistScript() {
        return "javascript:player.playVideoAt(0)";
    }

    public static String isPlaylistEnded() {
        return "javascript:window.Interface.playlistItems(player.getPlaylist());" +
                "window.Interface.currVidIndex(player.getPlaylistIndex());";
    }

    public static String resetPlaybackQuality(String quality) {
        return "javascript:player.setPlaybackQuality(\"" + quality + "\");";
    }

    public static String getVideosInPlaylist() {
        return "javascript:window.Interface.videosInPlaylist(player.getPlaylist());";
    }

    public static String getCuePlayList(String plist) {
        return "javascript:window.Interface.videosInPlaylist(" +
                "player.cuePlaylist(" +
                    plist +
                    ", 0, 0, small" +
                "));";
//        player.cuePlaylist(playlist:String|Array,
//                index:Number,
//                startSeconds:Number,
//                suggestedQuality:String):Void
    }
}
