import React, { useState, useRef } from "react";
import { Container, Row, Col, Button, Form } from "react-bootstrap";
import { AiOutlinePicture } from "react-icons/ai";
import profileUserImg from "../../../../assests/woman.jpg";
import api from "../../../../helpers/api";

const PostSection = (props) => {
  const [content, setContent] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState(null); // URL preview
  const fileInputRef = useRef(null);

  const handleFileSelect = (event) => {
    const files = event.target.files;
    if (files.length > 0) {
      const file = files[0];
      setSelectedFile(file);
      const url = URL.createObjectURL(file);
      setPreviewUrl(url);
      console.log("Selected file:", file);
      console.log("Preview URL:", url);
    }
  };

  const handlePost = async () => {
    if (!content && !selectedFile) {
      alert("Please provide content or select a file.");
      return;
    }

    const formData = new FormData();
    formData.append("post", JSON.stringify({ content, category: "social" }));
    if (selectedFile) {
      formData.append("file", selectedFile);
    }

    try {
      const response = await api.post(
        "http://localhost:8080/post/create",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      console.log("Post created successfully:", response.data);
      props.onPost([response.data]);
      setContent("");
      setSelectedFile(null);
      setPreviewUrl(null); // Xóa preview sau khi đăng bài
    } catch (error) {
      console.error("Error creating post:", error);
    }
  };

  return (
    <Container style={{ width: "90%", margin: "0" }}>
      <Row>
        <Col className="mx-auto p-0">
          <div className="card my-4">
            <div className="card-body">
              <div className="d-flex align-items-center">
                <img
                  src={profileUserImg}
                  className="profile-user-img me-2"
                  alt="User Profile"
                  style={{ width: "40px", height: "40px" }}
                />
                <Form.Control
                  type="text"
                  placeholder="What's on your mind?"
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                />
              </div>
              {previewUrl && (
                <div style={{ margin: "10px 0" }}>
                  {selectedFile.type.startsWith("image/") ? (
                    <img
                      src={previewUrl}
                      alt="Preview"
                      style={{
                        maxWidth: "100%",
                        maxHeight: "200px",
                        borderRadius: "8px",
                      }}
                    />
                  ) : selectedFile.type.startsWith("video/") ? (
                    <video
                      src={previewUrl}
                      controls
                      style={{
                        maxWidth: "100%",
                        maxHeight: "200px",
                        borderRadius: "8px",
                      }}
                    />
                  ) : (
                    <p>File không được hỗ trợ để xem trước.</p>
                  )}
                </div>
              )}
            </div>
            <div className="card-footer d-flex justify-content-between">
              <div>
                <Button
                  variant="light"
                  onClick={() => fileInputRef.current.click()}
                >
                  <AiOutlinePicture size={20} className="me-2" />
                  Photo/Video
                </Button>
                <input
                  type="file"
                  ref={fileInputRef}
                  style={{ display: "none" }}
                  onChange={handleFileSelect}
                />
              </div>
              <div>
                <Button variant="primary" onClick={handlePost}>
                  Post
                </Button>
              </div>
            </div>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default PostSection;
