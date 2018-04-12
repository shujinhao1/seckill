package com.shu.seckill.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shu.seckill.dto.SeckillResult;
import com.shu.seckill.entity.Seckill;
import com.shu.seckill.service.ISeckillService;

@Controller
public class SeckillController {
	@Autowired
    private ISeckillService seckillService;
	@RequestMapping("querySeckillList")
	public String querySeckillList(Model model) {
		List<Seckill> list = seckillService.querySeckillList();
		model.addAttribute("list", list);
		return "list";
	}
	@RequestMapping("/{seckillId}/detail")
	public String querySeckillById(@PathVariable("seckillId")Long seckillId,Model model) {
		 if (seckillId == null) {
	            return "redirect:/seckill/list";
	        }

		 Seckill seckill = seckillService.querySeckillById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }

        model.addAttribute("seckill", seckill);

        return "detail";
	}
	 //获取系统时间
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }
}
