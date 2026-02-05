package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.DateFilterForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.entity.Report;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    @Autowired
    CommentService commentService;

    final String DATE_FORMAT = "uuuu-MM-dd";
    final DateTimeFormatter  DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT).withResolverStyle(ResolverStyle.STRICT);

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(value = "since", required = false) String since,
                            @RequestParam(value = "until", required = false) String until) {
        ModelAndView mav = new ModelAndView();
        LocalDate sinceDate = null;
        LocalDate untilDate = null;

        try {
            if (since != null && !since.isBlank()) {
                sinceDate = LocalDate.parse(since, DATE_FORMATTER);
            }

            if (until != null && !until.isBlank()) {
                untilDate = LocalDate.parse(until, DATE_FORMATTER);
            }
        } catch (DateTimeParseException e) {
            since = "";
            until = "";
        }

        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findReportBetween(sinceDate, untilDate);
        List<CommentForm> commentData = commentService.findAllComment();

        DateFilterForm dateFilterForm = new DateFilterForm();
        dateFilterForm.setSince(since);
        dateFilterForm.setUntil(until);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("comments", commentData);
        mav.addObject("dateFilterForm", dateFilterForm);
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(
            @ModelAttribute("formModel")
            @Validated
            ReportForm reportForm,

            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("/new");
        }
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        reportService.deleteReport(id);
        commentService.deleteCommentByReportId(id);
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集画面表示処理
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        ReportForm report = reportService.findReport(id);
        // 編集する投稿をセット
        mav.addObject("formModel", report);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(
            @PathVariable Integer id,
            @ModelAttribute("formModel") @Validated ReportForm report,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("/edit");
        }
        // UrlParameterのidを更新するentityにセット
        report.setId(id);
        // 編集した投稿を更新
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }


    /*
     * 新規コメント画面表示
     */
    @GetMapping("/comment/new/{reportId}")
    public ModelAndView newComment(@PathVariable Integer reportId) {
        ModelAndView mav = new ModelAndView();
        ReportForm reportForm = reportService.findReport(reportId);
        CommentForm commentForm = new CommentForm();
        commentForm.setReportId(reportId);
        mav.setViewName("/comment_new");
        mav.addObject("report", reportForm);
        mav.addObject("formModel", commentForm);
        return mav;
    }

    /*
     * 新規コメント処理
     */
    @PostMapping("/comment/add")
    public ModelAndView addComment(
            @ModelAttribute("formModel") @Validated CommentForm commentForm,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()){
            ReportForm reportForm = reportService.findReport(commentForm.getReportId());
            ModelAndView mav = new ModelAndView("/comment_new");
            mav.addObject("report", reportForm);
            return mav;
        }
        commentService.saveComment(commentForm);
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集画面表示処理
     */
    @GetMapping("/comment/edit/{id}")
    public ModelAndView editComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        CommentForm commentForm = commentService.findComment(id);
        mav.addObject("formModel", commentForm);
        mav.setViewName("/comment_edit");
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/comment/update/{id}")
    public ModelAndView updateComment(
            @PathVariable Integer id,
            @ModelAttribute("formModel") @Validated CommentForm commentForm,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("/comment_edit");
        }
        commentForm.setId(id);
        commentService.saveComment(commentForm);
        return new ModelAndView("redirect:/");
    }

    /*
     * 返信削除処理
     */
    @DeleteMapping("/comment/delete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return new ModelAndView("redirect:/");
    }
}
