package com.vcb.master.controller;

import com.vcb.master.dto.FilterDTO;
import com.vcb.master.dto.MatchInputDTO;
import com.vcb.master.entity.MatchResult;
import com.vcb.master.service.MatchService;
import com.vcb.master.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
public class WebController {
    private final MatchService matchService;

    public WebController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, @ModelAttribute("filterDTO") FilterDTO filterDTO, Model model) {
        System.out.println(Util.getDate6("yyyy-MM-dd HH:mm:ss")+" index " + request.getRemoteAddr());
        List<MatchResult> matches;
        if(filterDTO.getFromDate()==null && filterDTO.getToDate()==null) {
            LocalDate localDate = LocalDate.now();
            Date firstDayOfMonth = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            matches = matchService.getByDateRangeAndPlayer(
                    firstDayOfMonth, new Date(), null);
        } else  {
            matches = matchService.getByDateRangeAndPlayer(
                    filterDTO.getFromDate(), filterDTO.getToDate(), filterDTO.getPlayerName());
        }
        matches.sort(
                Comparator.comparing(
                        MatchResult::getPlayedAt
                ).thenComparing(MatchResult::getPlayer1,Comparator.nullsLast(String::compareToIgnoreCase))
        );

        if(filterDTO.getFromDate()==null && filterDTO.getToDate()==null ){
            FilterDTO f = new FilterDTO();



            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1); // đặt ngày là ngày đầu tháng
            Date firstDayOfMonth = calendar.getTime();

            Date firstDayOfMonth1 = new Date();

            f.setFromDate(firstDayOfMonth1);

            f.setToDate(new Date());
            model.addAttribute("filterDTO", f);
        } else {
            model.addAttribute("filterDTO", filterDTO);
        }
        model.addAttribute("matches", matches);
        model.addAttribute("combinedStats", matchService.getCombinedStats1(matches));
        return "index";
    }


    @GetMapping("/total")
    public String total(Model model) {
        System.out.println(Util.getDate6("yyyy-MM-dd HH:mm:ss")+" total ");
        MatchInputDTO m =new MatchInputDTO();
        m.setMatchDate(new Date());
        model.addAttribute("matchInputDTO", m);

        //LocalDate localDate = LocalDate.now().withDayOfMonth(1);;
        LocalDate localDate = LocalDate.now();
        Date firstDayOfMonth = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        //System.out.println(firstDayOfMonth);

        List<MatchResult> matches = matchService.getByDateRangeAndPlayer(
                firstDayOfMonth, new Date(), null);
        matches.sort(
                Comparator.comparing(
                        MatchResult::getPlayedAt
                ).thenComparing(MatchResult::getPlayer1,Comparator.nullsLast(String::compareToIgnoreCase))
        );

        model.addAttribute("matches", matches);
        model.addAttribute("combinedStats", matchService.getCombinedStats1(matches));
        return "total";
    }

    @PostMapping("/submit")
    public String submitScore(HttpServletRequest request, @ModelAttribute MatchInputDTO matchInputDTO, Model model) {
        System.out.println(Util.getDate6("yyyy-MM-dd HH:mm:ss")+" submit " + request.getRemoteAddr());
        //System.out.println(matchInputDTO.getRawInput());
        try {
            //matchService.parseAndSave(matchInputDTO.getRawInput());
            matchService.parseAndSave(matchInputDTO.getRawInput(), matchInputDTO.getMatchDate());
            model.addAttribute("error", "Thành công: "+matchInputDTO.getRawInput());
        } catch (Exception e) {
            model.addAttribute("error", "Sai định dạng! VD: Trịnh 1-0 Minh");
        }
        MatchInputDTO m = new MatchInputDTO();
        m.setMatchDate(matchInputDTO.getMatchDate());
        model.addAttribute("matchInputDTO", m);

       // LocalDate localDate = LocalDate.now().withDayOfMonth(1);;
        LocalDate localDate = LocalDate.now();
        Date firstDayOfMonth = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        //System.out.println(firstDayOfMonth);

        List<MatchResult> matches = matchService.getByDateRangeAndPlayer(
                firstDayOfMonth, new Date(), null);
        matches.sort(
                Comparator.comparing(
                        MatchResult::getPlayedAt
                ).thenComparing(MatchResult::getPlayer1,Comparator.nullsLast(String::compareToIgnoreCase))
        );

        model.addAttribute("matches", matches);
        model.addAttribute("combinedStats", matchService.getCombinedStats1(matches));
        return "total";
    }

    @PostMapping("/delete/{id}")
    public String deleteMatch(@PathVariable Long id) {
        matchService.deleteById(id);
        return "redirect:/";
    }

}
