import React, { useEffect, useState } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import {
  Button,
  Row,
  Col,
  Form,
  ModalHeader,
  ModalBody,
  Alert,
  ModalFooter,
  Modal,
  CardBody,
  CardTitle,
  CardText,
  Container,
} from 'reactstrap';
import { TextFormat, ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getPublicNoteEntity } from './note.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useForm } from 'react-hook-form';
import Card from 'reactstrap/lib/Card';

export const PublicNotesDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const [showModal, setShowModal] = useState(true);
  useEffect(() => {}, []);
  const noteEntity = useAppSelector(state => state.note.entity);

  const handleSubmit = event => {
    event.preventDefault();
    dispatch(getPublicNoteEntity({ id: props.match.params.id, accessPassword: event.target[1].value }));
    setShowModal(prevState => !prevState);
  };
  return (
    <React.Fragment>
      <Modal isOpen={showModal} toggle={() => setShowModal(false)} backdrop="static" id="login-page" autoFocus={false}>
        <Form onSubmit={handleSubmit}>
          <ModalHeader id="login-title" data-cy="loginTitle" toggle={() => setShowModal(false)}>
            Enter password to access notes.
          </ModalHeader>
          <ModalBody>
            <Row>
              <Col md="12">
                <ValidatedField
                  name="password"
                  type="password"
                  label="Password"
                  placeholder="Your password"
                  required
                  data-cy="password"
                  validate={{ required: 'Password cannot be empty!' }}
                />
              </Col>
            </Row>
            <div className="mt-1">&nbsp;</div>
          </ModalBody>
          <ModalFooter>
            <Button color="primary" type="submit" data-cy="submit">
              Access
            </Button>
          </ModalFooter>
        </Form>
      </Modal>
      {noteEntity.content ? (
        <Container xs={12}>
          <div className="d-flex justify-content-center">
            <Row>
              <Col>
                <h2 data-cy="notesDetailsHeading">Notes</h2>
                <dl className="jh-entity-details">
                  <dt>
                    <span id="id">ID</span>
                  </dt>
                  <dd>{noteEntity.id}</dd>
                  <dt>
                    <span id="content">Content</span>
                  </dt>
                  <Card style={{ width: '18rem' }}>
                    <CardBody>
                      <CardText>{noteEntity.content}</CardText>
                    </CardBody>
                  </Card>
                  <dt>
                    <span id="expirationDate">Access Expiration Date</span>
                  </dt>
                  <dd>
                    {noteEntity.expirationDate ? (
                      <TextFormat value={noteEntity.expirationDate} type="date" format={APP_DATE_FORMAT} />
                    ) : null}
                  </dd>
                </dl>
              </Col>
            </Row>
          </div>
        </Container>
      ) : (
        <Row className="justify-content-center">
          <Button
            color="primary"
            className="link-password-btn"
            onClick={() => {
              setShowModal(prevState => !prevState);
            }}
          >
            <FontAwesomeIcon icon="lock" /> <span className="d-none d-md-inline">Enter Password</span>
          </Button>
        </Row>
      )}
    </React.Fragment>
  );
};

export default PublicNotesDetail;
