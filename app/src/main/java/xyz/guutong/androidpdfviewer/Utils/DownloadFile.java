package xyz.guutong.androidpdfviewer.Utils;

/* credit by https://github.com/voghDev/PdfViewPager */
public interface DownloadFile {
    public void download(String url, String destinationPath);

    public interface Listener{
        public void onSuccess(String url, String destinationPath);
        public void onFailure(Exception e);
        public void onProgressUpdate(int progress, int total);
    }
}
