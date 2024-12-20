package com.bgvacc.web.api.vateud;

import static com.bgvacc.web.api.APIConstants.VATEUD_API_KEY_HEADER_KEY;
import com.bgvacc.web.api.Api;
import com.bgvacc.web.configurations.properties.AuthenticationProperties;
import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.models.portal.trainings.CreateTrainingNoteModel;
import com.bgvacc.web.requests.portal.trainings.CreateTrainingNoteRequest;
import com.bgvacc.web.responses.trainings.CreateTrainingNoteResponse;
import com.bgvacc.web.vatsim.members.VatsimMemberTrainingStaff;
import com.bgvacc.web.vatsim.vateud.ControllerNotes;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class VatEudCoreApi extends Api {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private AuthenticationProperties authProps;

  public VatEudUser getMemberDetails(Long cid) {

    final String url = "https://core.vateud.net/api/facility/user/" + cid;

    return doRequest(Methods.GET, url, null, VatEudUser.class, null, VATEUD_API_KEY_HEADER_KEY, authProps.getVatEudApiKey());
  }

  public ControllerNotes getMemberNotes(Long cid) {

    final String url = "https://core.vateud.net/api/facility/user/" + cid + "/notes";

    return doRequest(Methods.GET, url, null, ControllerNotes.class, null, VATEUD_API_KEY_HEADER_KEY, authProps.getVatEudApiKey());
  }

  public CreateTrainingNoteResponse createTrainingNote(CreateTrainingNoteModel ctnm) {

    CreateTrainingNoteRequest ctnr = new CreateTrainingNoteRequest();
    ctnr.setInstructorCid(ctnm.getInstructorCid());
    ctnr.setPosition(ctnm.getPosition());
    ctnr.setNote(ctnm.getNote());
    ctnr.setSessionType(ctnm.getSessionType());
    ctnr.setFile(ctnm.getFile());

    final String url = "https://core.vateud.net/api/facility/user/" + ctnm.getUserCid() + "/notes";

    return doRequest(Methods.POST, url, ctnr, CreateTrainingNoteResponse.class, null, VATEUD_API_KEY_HEADER_KEY, authProps.getVatEudApiKey());
  }

  public VatsimMemberTrainingStaff getTrainingStaff() {

    final String url = "https://core.vateud.net/api/facility/training/staff";

    return doRequest(Methods.GET, url, null, VatsimMemberTrainingStaff.class, null, VATEUD_API_KEY_HEADER_KEY, authProps.getVatEudApiKey());
  }
}
