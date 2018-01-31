package scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.text.AbstractDocument;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Fetch paper information from DBLP
 */
public class PaperCollector {
    public static void main(String[] args) throws IOException {
//        MakeForError();
//        String comsurEntry = "http://dblp.uni-trier.de/db/journals/comsur/"; // no password related papers
//        String tifsEntry = "http://dblp.uni-trier.de/db/journals/tifs/";
//        String tdscEntry = "http://dblp.uni-trier.de/db/journals/tdsc/";
//        FetchForJournal(tdscEntry);

//        String[] confs = {"ccs", "sp", "ndss", "uss"};
//        int[] years = {2013, 2014, 2015, 2016, 2017};
//        FetchConfPapers("./src/scrapper/pwd-paper-five-year.txt", "./src/scrapper/error.txt", confs, years);
        FetchBibTex("./src/scrapper/pwd-five.bib", "./src/scrapper/pwd-paper-five-year.txt");
    }

    public static void FetchForJournal(String journalEntry) throws IOException {
        // no password related paper in IEEE Communication Survey & Tutorials
//        String comsurEntry = "http://dblp.uni-trier.de/db/journals/comsur/";
//        String tifsEntry = "http://dblp.uni-trier.de/db/journals/tifs/";
//        String tdscEntry = "http://dblp.uni-trier.de/db/journals/tdsc/";
        List<JournalInfo> journals = new ArrayList<>();
//        File tempFile = new File("./src/scrapper/journal.txt");
//        if (tempFile.exists()){
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile)));
//            String line;
//            while ((line = br.readLine()) != null){
//                String[] temp = line.split("\t");
//                if (temp.length == 2){
//                    journals.add(new JournalInfo(temp[0], temp[1]));
//                }else
//                    System.out.println(line);
//            }
//            br.close();
//        }else{
//            journals = JournalPage(journalEntry);
//        }
        journals = JournalPage(journalEntry);
        BufferedWriter errorPage = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("error.txt", true)));
        List<PaperInfo> papers = new ArrayList<>();
        for (JournalInfo journal: journals){
            try{
                papers.addAll(ContentPage(journal.dblpUrl, 0, journal.volumnInfo));
            }catch (Exception e){
                System.out.println("parse journal page error:");
                System.out.println(journal.dblpUrl);
                errorPage.write(journal.toString());
                errorPage.newLine();
            }
        }
        errorPage.close();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("paper-info.txt", true)));
        for (PaperInfo paper: papers){
            bw.write(paper.toString());
            bw.newLine();
        }
        bw.close();
    }

    public static void GetJournalEntries(String journalEntry) throws IOException {
        List<JournalInfo> journals = JournalPage(journalEntry);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./src/scrapper/journal.txt")));
        for(JournalInfo journal: journals){
            bw.write(journal.toString());
            bw.newLine();
        }
        bw.close();
    }

    public static void FetchConfPapers(String outputFilename, String errorFilename, String[] confAbbreviations, int[] years) throws IOException {
        BufferedWriter paperPage = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilename, true)));
        BufferedWriter errorPage = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errorFilename, true)));
        for (String conf: confAbbreviations){
            System.out.println("parsing conf: " + conf);
            for (int year: years){
                String url = dblpConfEntry(conf, year);
                try{
                    List<PaperInfo> papers = ContentPage(url, year, null);
                    for (PaperInfo paperInfo: papers){
                        paperPage.write(paperInfo.toString());
                        paperPage.newLine();
                        paperPage.flush();
                    }
                }catch (Exception e){
                    String errormsg = "parse conf page error: " + url;
                    System.out.println(errormsg);
                    errorPage.write(errormsg);
                    errorPage.newLine();
                    errorPage.flush();
                }
            }
        }
        paperPage.close();
        errorPage.close();
    }

    public static void FetchBibTex(String outputFilename, String paperFilename) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(paperFilename)));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilename, true)));
        String line;
        while ((line = br.readLine()) != null){
            String[] splits = line.split("\t");
            String dblpkey = splits[splits.length-1];
            if (!dblpkey.startsWith("conf") && !dblpkey.startsWith("journals")){
                System.out.println("no dblp key found : " + splits[0] + "\t" + dblpkey);
            }else try{
                bw.write(BibTexPage(dblpkey));
                bw.newLine();
                bw.flush();
            }catch (IOException e){
                System.out.println("error: " + dblpkey + "\t" + splits[0]);
            }
        }
        br.close();
        bw.close();
    }

    public static String BibTexPage(String dblpKey) throws IOException {
        String url = "http://dblp.uni-trier.de/rec/bibtex/" + dblpKey;
        Document doc = Jsoup.connect(url).get();
        Element bibsec = doc.getElementById("bibtex-section");
        return bibsec.child(0).text();
    }

    // legacy code that separately fetch entry and fetch paper
    public static void FetchForConference(String errorFilename) throws IOException {
        String ccsEntry = "http://dblp.uni-trier.de/db/conf/ccs/";
        String spEntry = "http://dblp.uni-trier.de/db/conf/sp/";
        String usenixEntry = "http://dblp.uni-trier.de/db/conf/uss/";
        String ndssEntry = "http://dblp.uni-trier.de/db/conf/ndss/";
        String[] topfour = {ccsEntry, spEntry, usenixEntry, ndssEntry};
        BufferedWriter confPage = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("conf.txt", true)));
        BufferedWriter errorPage = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errorFilename, true)));
        List<ConferenceInfo> confEntries = new ArrayList<>();
        for (String entry: topfour){
            try{
                // legacy code that fetch entry url of each conf from dblp search site
                List<ConferenceInfo> entryList = ConferencePage(entry);
                for (ConferenceInfo confinfo: entryList) {
                    confPage.write(confinfo.dblpUrl);
                    confPage.newLine();
                }
                confEntries.addAll(entryList);
            }catch(Exception e){
                System.out.println(entry);
                e.printStackTrace();
                errorPage.write("entry:" + entry);
                errorPage.newLine();
            }
        }
        confPage.close();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("paper-info.txt", true)));
        for (ConferenceInfo conf: confEntries){
            System.out.println("parsing: " + conf.dblpUrl);
            List<PaperInfo> papers;
            try {
//                papers = ContentPage(conf);
                papers = ContentPage(conf.dblpUrl, conf.year, null);
                for (PaperInfo paperInfo: papers){
                    bw.write(paperInfo.toString());
                    bw.newLine();
                }
            }catch(Exception e){
                System.out.println("parse page error:");
                System.out.println(conf.dblpUrl);
                errorPage.write(conf.toString());
                errorPage.newLine();
            }
        }
        bw.close();
        errorPage.close();
    }

    private static String dblpConfEntry(String abrv, int year){
        return String.format("http://dblp.uni-trier.de/db/conf/%s/%s%d", abrv, abrv, year);
    }

    public static void MakeForError() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./src/scrapper/error.txt")));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("error-reload.txt", true)));
        String line;
        while ((line = br.readLine()) != null){
            if (line.contains("entry"))
                continue; //TO be finished
            String[] temp = line.split("\t");
            ConferenceInfo conf = new ConferenceInfo(temp[0], Integer.parseInt(temp[1]));
//            List<PaperInfo> papers = ContentPage(conf);
            List<PaperInfo> papers = ContentPage(conf.dblpUrl, conf.year, null);
            for (PaperInfo paperInfo: papers){
                bw.write(paperInfo.toString());
                bw.newLine();
            }
        }
        br.close();
        bw.close();
    }

    public static List<JournalInfo> JournalPage(String urlStr) throws IOException {
        List<JournalInfo> result = new ArrayList<>();
        Document doc = Jsoup.connect(urlStr).get();
        Elements mainChildren = doc.getElementById("main").children();
        for (Element child: mainChildren){
            if (child.tagName().equals("ul")){
                Elements lis = child.children();
                for (Element li: lis){
                    Element link = li.child(0);
                    System.out.println(link.text());
                    System.out.println(link.attr("href"));
                    result.add(new JournalInfo(link.attr("href"), link.text()));
                }
            }
        }
        return result;
    }

    public static List<ConferenceInfo> ConferencePage(String urlStr) throws IOException {
        List<ConferenceInfo> urlList = new ArrayList<>();
        Document doc = Jsoup.connect(urlStr).get();
        Elements data = doc.getElementsByClass("data");
        for (Element ele: data){
            Element title = ele.getElementsByClass("title").get(0);
            if (!title.text().toLowerCase().contains("symposium") || title.text().toLowerCase().contains("workshop"))
                continue;
            int childSize = ele.children().size();
            Element link = ele.child(childSize-1);
            Element parent = link.parent();
            Element year = null;
            for (Element child: parent.children()){
                if (child.attr("itemprop").equals("datePublished"))
                    year = child;
            }
            urlList.add(new ConferenceInfo(link.attr("href"), year==null?0:Integer.parseInt(year.text())));
        }
        return urlList;
    }

    public static List<PaperInfo> ContentPage(String urlStr, int year, String info) throws IOException {
        System.out.println("parsing content page: " + urlStr);
        List<PaperInfo> papers = new ArrayList<>();
        Document doc = Jsoup.connect(urlStr).get();
        Elements titles = doc.getElementsByClass("title");
        for (Element titleEle: titles){
            if (titleEle.text().toLowerCase().contains("password")){
                // fetch title
                String title = titleEle.text();
                Element nav = titleEle.parent().previousElementSibling();
                String link = nav.child(0).child(0).child(1).child(1).child(0).child(0).attr("href");
                List<String> authors = new ArrayList<>();
                String page = "";
                Element parent = titleEle.parent();
                for(Element child: parent.children()){
                    // fetch authors
                    if (child.attr("itemprop").equals("author")){
                        authors.add(child.text());
                    }else if (child.attr("itemprop").equals("pagination")){
                        // fetch page
                        page = child.text();
                    }
                }
                // fetch bib url
                String dblpkey = "";
                try {
                    Element navEle = parent.previousElementSibling();
//                    System.out.println(navEle.tagName());
//                    System.out.println(navEle.child(0).tagName());
//                    System.out.println(navEle.child(0).child(1).tagName());
//                    System.out.println(navEle.child(0).child(1).child(1).tagName());
//                    System.out.println(navEle.child(0).child(1).child(1).child(3).tagName());
//                    System.out.println(navEle.child(0).child(1).child(1).child(3).child(0).child(0).html());
                    dblpkey = navEle.child(0).child(1).child(1).child(3).child(0).child(0).html();
//                    System.out.println(dblpkey);
                }catch(Exception e){
                    System.out.println("no dblp key found for " + title);
                }
                PaperInfo paper;
                if (info == null)
                    paper = new PaperInfo(title, authors, year, link, page, dblpkey);
                else
                    paper = new PaperInfo(title, authors, info, link, page, dblpkey);
                papers.add(paper);
                System.out.println(paper.toString());
            }else{
//                System.out.println("not related: " + titleEle.text());
            }
        }
        return papers;
    }
}

class ConferenceInfo{
    String dblpUrl;
    int year;
    ConferenceInfo(String dblpUrl, int year){
        this.dblpUrl = dblpUrl;
        this.year = year;
    }
    public String toString(){return this.dblpUrl + "\t"+ this.year;}
}

class JournalInfo{
    String dblpUrl;         // journal url in dblp
    String volumnInfo;
    JournalInfo(String dblpUrl, String volumnInfo){
        this.dblpUrl = dblpUrl;
        this.volumnInfo = volumnInfo;
    }
    public String toString(){return this.dblpUrl + "\t" + this.volumnInfo;}
}

class PaperInfo {
    String title;           // paper title
    List<String> authors;   // author list with full names
    int year;               // publish year
    String url;             // fetch url (maybe inaccessible)
    String info;            // volume and year info of journal paper
    String page;            // page number in conference or journal (empty if null)
    String dblpKey;          // dblp key for BibTex (maybe null)
    PaperInfo(String title, List<String> authors, int year, String url, String page, String dblpKey){
        this.title = title;
        this.authors = authors;
        this.url = url;
        this.year = year;
        this.page = page;
        this.dblpKey = dblpKey;
    }
    PaperInfo(String title, List<String> authors, String info, String url, String page, String dblpKey){
        this.title = title;
        this.authors = authors;
        this.url = url;
        this.info = info;
        this.page = page;
        this.dblpKey = dblpKey;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append("\t");
        for (String author: authors) {
            sb.append(author);
            sb.append(",");
        }
        sb.delete(sb.length()-1, sb.length());
        sb.append("\t");
        sb.append(year);
        sb.append("\t");
        sb.append(url);
        sb.append("\t");
        sb.append(page);
        sb.append("\t");
        sb.append(dblpKey == null ? "" : dblpKey);
        if (info != null){
            sb.append("\t");
            sb.append(info);
        }
        return sb.toString();
    }
}