import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './note.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const NoteDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const noteEntity = useAppSelector(state => state.note.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="noteDetailsHeading">Note</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{noteEntity.id}</dd>
          <dt>
            <span id="content">Content</span>
          </dt>
          <dd>{noteEntity.content}</dd>
          <dt>
            <span id="password">Password</span>
          </dt>
          <dd>{noteEntity.password}</dd>
          <dt>
            <span id="link">Link</span>
          </dt>
          <dd>{noteEntity.link}</dd>
          <dt>
            <span id="expirationDate">Expiration Date</span>
          </dt>
          <dd>
            {noteEntity.expirationDate ? <TextFormat value={noteEntity.expirationDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{noteEntity.user ? noteEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/note" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/note/${noteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default NoteDetail;
