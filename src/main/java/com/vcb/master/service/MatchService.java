package com.vcb.master.service;

import com.vcb.master.entity.MatchResult;
import com.vcb.master.repository.MatchResultRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MatchService {
    private final MatchResultRepository repository;

    public MatchService(MatchResultRepository repository) {
        this.repository = repository;
    }

    public MatchResult parseAndSave(String input, Date matchDate) {
        Pattern pattern = Pattern.compile("([\\p{L} ]+)\\s(\\d+)\\s*-\\s*(\\d+)\\s([\\p{L} ]+)");
        Matcher matcher = pattern.matcher(input.trim());

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Sai định dạng.");
        }

        String team1Str = matcher.group(1).trim();
        int score1 = Integer.parseInt(matcher.group(2));
        int score2 = Integer.parseInt(matcher.group(3));
        String team2Str = matcher.group(4).trim();

        String[] team1 = team1Str.split("\\s+");
        String[] team2 = team2Str.split("\\s+");
        boolean isDoubles = team1.length == 2 && team2.length == 2;

        String player1 = team1[0];
        String player2= team1.length == 2 ? team1[1] : null;
        String player3=team2[0];
        String player4=team2.length == 2 ? team2[1] : null;

        Date playedAt = matchDate;

        MatchResult match = new MatchResult(player1,  player2,  player3,  player4, score1, score2,  isDoubles,  playedAt);

        return repository.save(match);
    }

    public List<MatchResult> getAll() {
        return repository.findAll(Sort.by("playedAt"));
    }


    public Map<String, int[]> getCombinedStats() {
        Map<String, int[]> stats = new HashMap<>();
        List<MatchResult> matches = repository.findAll();

        for (MatchResult match : matches) {
            boolean team1Won = match.getScoreTeam1() > match.getScoreTeam2();
            boolean isDoubles = match.isDoubles();

            List<String> team1 = new ArrayList<>();
            if (match.getPlayer1() != null) team1.add(match.getPlayer1());
            if (isDoubles && match.getPlayer2() != null) team1.add(match.getPlayer2());

            List<String> team2 = new ArrayList<>();
            if (match.getPlayer3() != null) team2.add(match.getPlayer3());
            if (isDoubles && match.getPlayer4() != null) team2.add(match.getPlayer4());

            for (String player : team1) {
                int[] s = stats.getOrDefault(player, new int[5]);
                if (team1Won) s[isDoubles ? 0 : 2]++; else s[isDoubles ? 1 : 3]++;
                stats.put(player, s);
            }
            for (String player : team2) {
                int[] s = stats.getOrDefault(player, new int[5]);
                if (!team1Won) s[isDoubles ? 0 : 2]++; else s[isDoubles ? 1 : 3]++;
                stats.put(player, s);
            }

            for (String loser : team1Won ? team2 : team1) {
                int[] s = stats.getOrDefault(loser, new int[5]);
                s[4] += isDoubles ? 10000 : 20000;
                stats.put(loser, s);
            }
        }

        return stats;
    }

    public Map<String, int[]> getCombinedStats1(List<MatchResult> matches) {
        Map<String, int[]> stats = new HashMap<>();
        //List<MatchResult> matches = repository.findAll();

        for (MatchResult match : matches) {
            boolean team1Won = match.getScoreTeam1() > match.getScoreTeam2();
            boolean isDoubles = match.isDoubles();

            List<String> team1 = new ArrayList<>();
            if (match.getPlayer1() != null) team1.add(match.getPlayer1());
            if (isDoubles && match.getPlayer2() != null) team1.add(match.getPlayer2());

            List<String> team2 = new ArrayList<>();
            if (match.getPlayer3() != null) team2.add(match.getPlayer3());
            if (isDoubles && match.getPlayer4() != null) team2.add(match.getPlayer4());

            for (String player : team1) {
                int[] s = stats.getOrDefault(player, new int[5]);
                if (team1Won) s[isDoubles ? 0 : 2]++; else s[isDoubles ? 1 : 3]++;
                stats.put(player, s);
            }
            for (String player : team2) {
                int[] s = stats.getOrDefault(player, new int[5]);
                if (!team1Won) s[isDoubles ? 0 : 2]++; else s[isDoubles ? 1 : 3]++;
                stats.put(player, s);
            }

            for (String loser : team1Won ? team2 : team1) {
                int[] s = stats.getOrDefault(loser, new int[5]);
                s[4] += isDoubles ? 10000 : 20000;
                stats.put(loser, s);
            }
        }


        // Sắp xếp theo phần tử thứ 5 (index 4) từ cao xuống thấp
        List<Map.Entry<String, int[]>> sortedList = stats.entrySet()
        .stream()
        .sorted(Comparator.comparingInt((Map.Entry<String, int[]> e) -> e.getValue()[4]).reversed())
        .collect(Collectors.toList());



        Map<String, int[]> resultMap = sortedList.stream()
        .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,// xử lý trường hợp trùng key (nếu có)
        LinkedHashMap::new// giữ thứ tự đã sắp xếp
         ));


        return resultMap;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<MatchResult> getByDateRangeAndPlayer(Date fromDate, Date toDate, String playerName) {
        //if (fromDate == null || toDate == null) return getAll();
        if (playerName == null || playerName.isBlank()) {
            return repository.findByPlayedAtBetween(fromDate, toDate);
        } else {
            return repository.findByData(
                    fromDate, toDate,
                    playerName);
        }
    }

}