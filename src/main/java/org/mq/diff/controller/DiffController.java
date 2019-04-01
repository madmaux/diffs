package org.mq.diff.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.Arrays;
import javax.validation.Valid;
import org.mq.diff.domain.Side;
import org.mq.diff.dto.ContentDTO;
import org.mq.diff.dto.DiffEdDTO;
import org.mq.diff.dto.DiffResultDTO;
import org.mq.diff.service.DataComparator;
import org.mq.diff.service.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DiffController: Handle all the requests for the diff service
 *
 * @author MQ
 * @version 0.0.1
 */
@RestController
@EnableHypermediaSupport(type = HypermediaType.HAL)
@RequestMapping("/v1/diff")
public class DiffController {
  private static final String LINK_SIDES_NAME = "sides";
  private static final String LINK_DIFFS_NAME = "diff-results";
  private static final String LINK_NEXT_SIDE_NAME = "next-side";

  @Autowired
  DataManager dataManager;

  @Autowired
  DataComparator dataComparator;

  /**
   *
   * leftContent: create and update left content data for the diff comparator
   *
   * @param id
   * @param diffEdDTO
   * @return hateos resource of type ContentDTO
   */
  @PostMapping(value = "/{id}/left", produces = {"application/hal+json"})
  public Resource<ContentDTO> leftContent(
      @PathVariable String id,
      @RequestBody @Valid DiffEdDTO diffEdDTO
  ) {

    return handleContent(id, diffEdDTO, Side.LEFT);
  }

  /**
   *
   * rightContent: create and update right content data for the diff comparator
   *
   * @param id
   * @param diffEdDTO
   * @return hateos resource of type ContentDTO
   */
  @PostMapping(value = "/{id}/right", produces = {"application/hal+json"})
  public Resource<ContentDTO> rightContent(
      @PathVariable String id,
      @RequestBody @Valid DiffEdDTO diffEdDTO
  ) {

    return handleContent(id, diffEdDTO, Side.RIGHT);
  }

  /**
   *
   * compare: compared the two stored content data (left and right sides) and returns their differences
   *
   * @param id
   * @return hateos resource of type DiffResultDTO
   */
  @GetMapping(value = "/{id}", produces = {"application/json"})
  public Resource<DiffResultDTO> compare(@PathVariable String id) {

    final var diffResultDTO = dataComparator.compare(id);

    var links = Arrays.asList(
        linkTo(DiffController.class).slash(id).slash(Side.LEFT.name().toLowerCase())
            .withRel(LINK_SIDES_NAME),
        linkTo(DiffController.class).slash(id).slash(Side.RIGHT.name().toLowerCase())
            .withRel(LINK_SIDES_NAME),
        linkTo(DiffController.class).slash(id).withSelfRel()
    );
    var resource = new Resource<>(diffResultDTO, links);

    return resource;
  }

  /**
   *
   * handleContent: take request data from leftContent and rightContent endpoints and pass it to
   * the dataManager service to me process
   *
   * @param id
   * @param diffEdDTO
   * @param side
   * @return hateos resource of type ContentDTO
   */
  private Resource<ContentDTO> handleContent(String id, DiffEdDTO diffEdDTO, Side side) {

    final var contentDTO = dataManager.save(id, diffEdDTO, side);

    var links = Arrays.asList(
        linkTo(DiffController.class).slash(id).slash(getOppositeSide(side).name().toLowerCase())
            .withRel(LINK_NEXT_SIDE_NAME),
        linkTo(DiffController.class).slash(id).withRel(LINK_DIFFS_NAME)
    );
    var resource = new Resource<>(contentDTO, links);

    return resource;
  }

  /**
   *
   * geOppositeSide: return the opposite Side of a given Side
   *
   * @param side
   * @return Side
   */
  private Side getOppositeSide(Side side) {

    return Side.values()[side.ordinal() == 0 ? 1 : 0];
  }
}
