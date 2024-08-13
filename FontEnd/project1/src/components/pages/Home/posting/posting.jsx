import React, { useState } from 'react';
import { Container, Row, Col, Button, Form } from 'react-bootstrap';
import { AiOutlinePicture } from 'react-icons/ai';
import profileUserImg from '../../../../assests/woman.jpg'
import api from '../../../../helpers/api';
import PostEntity from '../../../../entites/PostEntity';
import Cookies from 'js-cookie';
const PostSection = (props) => {
  const [content , setContent] = useState('');
  const handlePost = async (content) => {
    const newPost = new PostEntity(content, 'social');
    props.onPost(newPost);


    const data = await api.post(`http://localhost:8080/post/create`, newPost);
    console.log(data)
    setContent('');
  };


  return (
    <Container style={{ width: '90%' , margin: '0' }} 
    >
      <Row >
        <Col  className="mx-auto p-0 ">
          <div className="card my-4 " >
            <div className="card-body">
              <div className="d-flex align-items-center">
              <img src={profileUserImg} className="profile-user-img me-2" alt="User Profile" style={{ width: '40px', height: '40px' }} />
                <Form.Control
                  type="text"
                  placeholder="What's on your mind?"
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                />
              </div>
            </div>
            <div className="card-footer d-flex justify-content-between">
              <div>
                <Button variant="light">
                  <AiOutlinePicture size={20} className="me-2" />
                  Photo/Video
                </Button>
              </div>
              <div>
                <Button variant="primary" onClick={() => {
                  handlePost(content);
                }}>Post</Button>
              </div>
            </div>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default PostSection;
