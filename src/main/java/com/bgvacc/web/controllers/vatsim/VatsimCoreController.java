package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import com.bgvacc.web.vatsim.atc.VatsimATCInfo;
import com.bgvacc.web.vatsim.memory.Memory;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class VatsimCoreController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private VatEudCoreApi vatEudCoreApi;

  @GetMapping("/atc/status/{atc}")
  public String getCallsignStatusCard(@PathVariable("atc") String atc, Model model) {

    final String callsign = atc.replace("-", "_").toUpperCase();

    VatsimATC position = getOnlinePosition(callsign);
//    VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(vmsv.getUserCid());

    VatsimATCInfo atcInfo = getPositionFrequency(callsign);

    model.addAttribute("atcInfo", atcInfo);
    model.addAttribute("position", position);

    return "atc/controller-status :: #atcStatus";
  }

  @GetMapping("/atc/bg-online/count")
  @ResponseBody
  private Long getOnlineBGControllersCount() {
    Memory memory = Memory.getInstance();
    return Long.valueOf(memory.getOnlineATCListSize(true));
  }

  private VatsimATCInfo getPositionFrequency(String callsign) {

    switch (callsign.toUpperCase()) {
      case "LBSR_CTR":
        return new VatsimATCInfo(callsign, "Sofia Control", "131.225");
      case "LBSR_V_CTR":
        return new VatsimATCInfo(callsign, "Sofia Control", "134.700");
      case "LBSF_TWR":
      case "LBSF_T_TWR":
      case "LBSF_M_TWR":
      case "LBSF_X_TWR":
        return new VatsimATCInfo(callsign, "Sofia Tower", "118.100");
      case "LBSF_APP":
      case "LBSF_T_APP":
      case "LBSF_M_APP":
      case "LBSF_X_APP":
        return new VatsimATCInfo(callsign, "Sofia Approach", "123.700");
      case "LBBG_TWR":
        return new VatsimATCInfo(callsign, "Burgas Tower", "118.000");
      case "LBBG_APP":
        return new VatsimATCInfo(callsign, "Burgas Approach", "125.100");
      case "LBWN_TWR":
        return new VatsimATCInfo(callsign, "Varna Tower", "119.500");
      case "LBWN_APP":
        return new VatsimATCInfo(callsign, "Varna Approach", "124.600");
      case "LBPD_TWR":
        return new VatsimATCInfo(callsign, "Plovdiv Tower", "133.600");
      case "LBGO_TWR":
        return new VatsimATCInfo(callsign, "Gorna Tower", "133.500");
    }

    return null;
  }

  private VatsimATC getOnlinePosition(String position) {

    for (VatsimATC onlineBulgarianATC : getAllVatsimBGOnlineATCs()) {
      if (onlineBulgarianATC.getCallsign().equalsIgnoreCase(position)) {

        VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(onlineBulgarianATC.getId());

        if (memberDetails != null) {
          onlineBulgarianATC.setFirstName(memberDetails.getData().getFirstName());
          onlineBulgarianATC.setLastName(memberDetails.getData().getLastName());
        }
        return onlineBulgarianATC;
      }
    }

    return null;
  }

  public List<VatsimATC> getAllVatsimBGOnlineATCs() {
    Memory memory = Memory.getInstance();
    return memory.getAllOnlineATCsList(true);
  }

  private List<VatsimATC> getAllVatsimOnlineATCs() {

    Memory memory = Memory.getInstance();

    return memory.getAllOnlineATCsList();
  }

//  @GetMapping("/atc/bg-online")
//  @ResponseBody
//  private List<VatsimATC> getAllVatsimBulgarianOnlineATCs() {
//
//    return getAllVatsimBGOnlineATCs();
//  }
}
