package com.chsapps.yt_hongjinyoung.ui.adapter.viewholder;

import com.chsapps.yt_hongjinyoung.data.SongData;

public interface SongListHolderListener {
    void playSong(SongData song);
    void deleteSong(SongData song);
    void shareSong(SongData song);
    void saveSong(SongData song);
}
