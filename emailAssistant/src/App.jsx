import { useState } from 'react';
import { Container, TextField, Button, Typography, CircularProgress, Alert, MenuItem } from '@mui/material';
import axios from 'axios';

function App() {
  const [emailContent, setEmailContent] = useState('');
  const [tone, setTone] = useState('');
  const [generatedReply, setGeneratedReply] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleGenerateReply = async () => {
    setLoading(true);
    setError('');
    setGeneratedReply('');
    
    try {
      const response = await axios.post('http://localhost:8080/api/email/generate', {
        emailContent,
        tone,
      });
      setGeneratedReply(response.data);
    } catch (err) {
      setError('Failed to generate reply. Please try again.');
    }
    
    setLoading(false);
  };

  return (
    <Container maxWidth="md" sx={{ px: 4, py: 4 }}>
      <Typography variant="h4" gutterBottom>Email Reply Generator</Typography>
      <TextField
        label="Original Email"
        multiline
        rows={12}
        fullWidth
        margin="normal"
        value={emailContent}
        onChange={(e) => setEmailContent(e.target.value)}
      />
      <TextField
        select
        label="Tone"
        fullWidth
        margin="normal"
        value={tone}
        onChange={(e) => setTone(e.target.value)}
      >
        <MenuItem value="Professional">Professional</MenuItem>
        <MenuItem value="Casual">Casual</MenuItem>
        <MenuItem value="Friendly">Friendly</MenuItem>
      </TextField>
      <Button
        variant="contained"
        color="primary"
        fullWidth
        sx={{ mt: 2 }}
        onClick={handleGenerateReply}
        disabled={loading}
      >
        {loading ? <CircularProgress size={24} color="inherit" /> : 'Generate Reply'}
      </Button>
      
      {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
      {generatedReply && (
        <Alert severity="success" sx={{ mt: 2 }}>
          <Typography variant="body1"><strong>Generated Reply:</strong></Typography>
          <Typography variant="body2">{generatedReply}</Typography>
        </Alert>
      )}
    </Container>
  );
}

export default App;
